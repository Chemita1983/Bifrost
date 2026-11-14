package com.bifrost.infrastructure.steam;

import com.bifrost.application.port.out.SteamLibraryPort;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Calls the Steam Web API (IPlayerService/GetOwnedGames).
 * The API key travels in the URL, so Steam client errors are never included in our exceptions or logs.
 */
@Component
public class SteamApiClient implements SteamLibraryPort {

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
        OwnedGamesResponse body;
        try {
            body = restClient.get()
                    .uri(uri -> uri.path("/IPlayerService/GetOwnedGames/v1/")
                            .queryParam("key", apiKey)
                            .queryParam("steamid", steamId)
                            .queryParam("include_appinfo", true)
                            .queryParam("format", "json")
                            .build())
                    .retrieve()
                    .body(OwnedGamesResponse.class);
        } catch (RestClientResponseException ex) {
            String reason = steamReason(ex.getResponseBodyAsString());
            throw new IllegalStateException("Steam returned HTTP " + ex.getStatusCode().value()
                    + " when reading the library" + (reason.isEmpty() ? ". Check STEAM_API_KEY and STEAM_ID" : ": " + reason));
        } catch (RestClientException ex) {
            throw new IllegalStateException("Could not read the Steam library");
        }
        if (body == null || body.response() == null || body.response().games() == null) {
            throw new IllegalStateException("Steam did not return the library. Check that the profile game details are public");
        }
        return body.response().games().stream()
                .map(game -> new OwnedGame(game.appId(),
                        game.name() == null || game.name().isBlank() ? "Steam app " + game.appId() : game.name(),
                        game.minutesPlayed() == null ? 0 : game.minutesPlayed()))
                .toList();
    }

    /**
     * Steam explains errors in a small HTML page, for example "Missing required routing parameter".
     * Returns that text without tags, without the API key and limited to 200 characters.
     */
    private String steamReason(String body) {
        String text = HtmlUtils.htmlUnescape(body.replace(apiKey, "***")
                        .replaceAll("(?is)<title>.*?</title>", "")
                        .replaceAll("<[^>]*>", " "))
                .replaceAll("\\s+", " ")
                .strip();
        return text.length() > 200 ? text.substring(0, 200) : text;
    }

    /** Only the fields we use from Steam's JSON response. */
    record OwnedGamesResponse(Library response) {

        record Library(List<Game> games) {
        }

        record Game(@JsonProperty("appid") int appId,
                    String name,
                    @JsonProperty("playtime_forever") Integer minutesPlayed) {
        }
    }
}
