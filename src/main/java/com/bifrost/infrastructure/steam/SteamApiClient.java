package com.bifrost.infrastructure.steam;

import com.bifrost.application.port.out.SteamLibraryPort;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Calls the Steam Web API.
 * The API key travels in the URL, so Steam client errors are never included in our exceptions or logs.
 */
@Component
public class SteamApiClient implements SteamLibraryPort {

    /** Type of the store items that are games; others are software, DLC, demos... */
    private static final int GAME_TYPE = 0;
    private static final int STORE_BATCH_SIZE = 100;

    private final RestClient restClient;
    private final String apiKey;
    private final String steamId;

    public SteamApiClient(RestClient.Builder restClientBuilder,
                          @Value("${bifrost.steam.base-url}") String baseUrl,
                          @Value("${bifrost.steam.api-key}") String apiKey,
                          @Value("${bifrost.steam.steam-id}") String steamId) {
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.steamId = steamId;
    }

    @Override
    public List<OwnedGame> getOwnedGames() {
        if (apiKey.isBlank() || steamId.isBlank()) {
            throw new IllegalStateException("Configure STEAM_API_KEY and STEAM_ID to synchronize the Steam library");
        }
        OwnedGamesResponse body = call("reading the library", uri -> uri.path("/IPlayerService/GetOwnedGames/v1/")
                .queryParam("key", apiKey)
                .queryParam("steamid", steamId)
                .queryParam("include_appinfo", true)
                .queryParam("format", "json")
                .build(), OwnedGamesResponse.class);
        if (body == null || body.response() == null || body.response().games() == null) {
            throw new IllegalStateException("Steam did not return the library. Check that the profile game details are public");
        }
        return body.response().games().stream()
                .map(game -> new OwnedGame(game.appId(),
                        game.name() == null || game.name().isBlank() ? "Steam app " + game.appId() : game.name(),
                        game.minutesPlayed() == null ? 0 : game.minutesPlayed()))
                .toList();
    }

    /** Asks the Steam store (IStoreBrowseService/GetItems) for the type of the apps, in batches. */
    @Override
    public Set<Integer> findNonGames(List<Integer> appIds) {
        Set<Integer> nonGames = new HashSet<>();
        for (int start = 0; start < appIds.size(); start += STORE_BATCH_SIZE) {
            List<Integer> batch = appIds.subList(start, Math.min(start + STORE_BATCH_SIZE, appIds.size()));
            // The store needs a country; apps not sold there are unknown and therefore imported.
            String input = batch.stream().map(appId -> "{\"appid\":" + appId + "}")
                    .collect(Collectors.joining(",", "{\"ids\":[", "],\"context\":{\"country_code\":\"ES\"}}"));
            StoreItemsResponse body = call("reading the store information",
                    uri -> uri.path("/IStoreBrowseService/GetItems/v1/").queryParam("input_json", "{input}").build(input),
                    StoreItemsResponse.class);
            if (body == null || body.response() == null || body.response().storeItems() == null) {
                throw new IllegalStateException("Steam did not return the store information of the games");
            }
            body.response().storeItems().stream()
                    .filter(item -> item.type() != null && item.type() != GAME_TYPE)
                    .forEach(item -> nonGames.add(item.appId()));
        }
        return nonGames;
    }

    private <T> T call(String action, Function<UriBuilder, URI> uri, Class<T> responseType) {
        try {
            return restClient.get().uri(uri).retrieve().body(responseType);
        } catch (RestClientResponseException ex) {
            String reason = steamReason(ex.getResponseBodyAsString());
            throw new IllegalStateException("Steam returned HTTP " + ex.getStatusCode().value() + " when " + action
                    + (reason.isEmpty() ? ". Check STEAM_API_KEY and STEAM_ID" : ": " + reason));
        } catch (RestClientException ex) {
            throw new IllegalStateException("Could not connect to Steam when " + action);
        }
    }

    /**
     * Steam explains errors in a small HTML page, for example "Missing required routing parameter".
     * Returns that text without tags, without the API key and limited to 200 characters.
     */
    private String steamReason(String body) {
        String withoutKey = apiKey.isBlank() ? body : body.replace(apiKey, "***");
        String text = HtmlUtils.htmlUnescape(withoutKey
                        .replaceAll("(?is)<title>.*?</title>", "")
                        .replaceAll("<[^>]*>", " "))
                .replaceAll("\\s+", " ")
                .strip();
        return text.length() > 200 ? text.substring(0, 200) : text;
    }

    /* Only the fields we use from Steam's JSON responses. */

    record OwnedGamesResponse(Library response) {

        record Library(List<Game> games) {
        }

        record Game(@JsonProperty("appid") int appId,
                    String name,
                    @JsonProperty("playtime_forever") Integer minutesPlayed) {
        }
    }

    record StoreItemsResponse(StoreItems response) {

        record StoreItems(@JsonProperty("store_items") List<StoreItem> storeItems) {
        }

        record StoreItem(@JsonProperty("appid") int appId, Integer type) {
        }
    }
}
