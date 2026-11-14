package com.bifrost.infrastructure.steam;

import com.bifrost.application.port.out.SteamLibraryPort.OwnedGame;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/** Steam is simulated with MockRestServiceServer: no real connection is made. */
class SteamApiClientTest {

    private static final String KEY = "secret-key";
    private final RestClient.Builder builder = RestClient.builder();
    private final MockRestServiceServer steam = MockRestServiceServer.bindTo(builder).build();
    private final SteamApiClient client = new SteamApiClient(builder, "https://steam.test", KEY, "7656");

    @Test
    void readsOwnedGamesFromSteam() {
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/IPlayerService/GetOwnedGames/v1/")))
                .andExpect(method(GET))
                .andExpect(queryParam("key", KEY))
                .andExpect(queryParam("steamid", "7656"))
                .andExpect(queryParam("include_appinfo", "true"))
                .andRespond(withSuccess("""
                        {"response":{"game_count":3,"games":[
                          {"appid":620,"name":"Portal 2","playtime_forever":754,"img_icon_url":"x"},
                          {"appid":400,"name":"Portal"},
                          {"appid":10,"name":" ","playtime_forever":5}]}}
                        """, MediaType.APPLICATION_JSON));

        assertThat(client.getOwnedGames()).containsExactly(
                new OwnedGame(620, "Portal 2", 754),
                new OwnedGame(400, "Portal", 0),
                new OwnedGame(10, "Steam app 10", 5));
        steam.verify();
    }

    @Test
    void missingConfigurationFailsWithoutCallingSteam() {
        var unconfigured = new SteamApiClient(builder, "https://steam.test", "", "");

        assertThatThrownBy(unconfigured::getOwnedGames)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("STEAM_API_KEY");
        steam.verify();
    }

    @Test
    void privateProfileWithoutGamesIsReportedAsAnError() {
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/")))
                .andRespond(withSuccess("{\"response\":{}}", MediaType.APPLICATION_JSON));

        assertThatThrownBy(client::getOwnedGames).hasMessageContaining("public");
    }

    @Test
    void steamErrorsIncludeSteamsReasonWithoutTheApiKey() {
        // Same format as Steam's real answer to an invalid SteamID.
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/")))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_HTML).body("""
                        <html><head><title>Bad Request</title></head><body><h1>Bad Request</h1>
                        Missing required routing parameter &amp; <pre>key=secret-key</pre></body></html>
                        """));
        assertThatThrownBy(client::getOwnedGames)
                .hasMessage("Steam returned HTTP 400 when reading the library: "
                        + "Bad Request Missing required routing parameter & key=***")
                .hasMessageNotContaining(KEY)
                .hasNoCause();

        steam.reset();
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/")))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));
        assertThatThrownBy(client::getOwnedGames)
                .hasMessage("Steam returned HTTP 403 when reading the library. Check STEAM_API_KEY and STEAM_ID");

        steam.reset();
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/")))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR).body("x".repeat(500)));
        assertThatThrownBy(client::getOwnedGames)
                .hasMessageEndingWith(": " + "x".repeat(200));
    }

    @Test
    void connectionErrorsNeverExposeTheApiKey() {
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/")))
                .andRespond(withException(new IOException("connection refused")));
        assertThatThrownBy(client::getOwnedGames)
                .hasMessage("Could not connect to Steam when reading the library")
                .hasNoCause();
    }

    @Test
    void findSoftwareAsksTheStoreWithoutTheApiKeyAndReturnsOnlySoftware() {
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/IStoreBrowseService/GetItems/v1/")))
                .andExpect(method(GET))
                .andExpect(request -> {
                    String query = URLDecoder.decode(request.getURI().getRawQuery(), StandardCharsets.UTF_8);
                    assertThat(query).isEqualTo("input_json={\"ids\":[{\"appid\":620},{\"appid\":431960},"
                            + "{\"appid\":2778580},{\"appid\":99830},{\"appid\":211}],\"context\":{\"country_code\":\"ES\"}}");
                    assertThat(query).doesNotContain(KEY);
                })
                // Real Steam format: games are type 0, software 6, DLC 4; Crysis 2 (an old edition) is 14;
                // unknown apps come back without data. Only software is returned.
                .andRespond(withSuccess("""
                        {"response":{"store_items":[
                          {"item_type":0,"id":620,"success":1,"visible":true,"name":"Portal 2","appid":620,"type":0},
                          {"item_type":0,"id":431960,"success":1,"visible":true,"name":"Wallpaper Engine","appid":431960,"type":6},
                          {"item_type":0,"id":2778580,"success":1,"visible":true,"appid":2778580,"type":4},
                          {"item_type":0,"id":99830,"success":1,"visible":true,"name":"Crysis 2","appid":99830,"type":14},
                          {"success":15,"visible":false}]}}
                        """, MediaType.APPLICATION_JSON));

        assertThat(client.findSoftware(List.of(620, 431960, 2778580, 99830, 211))).containsExactly(431960);
        steam.verify();
    }

    @Test
    void findSoftwareAsksInBatchesOf100() {
        List<Integer> appIds = IntStream.rangeClosed(1, 150).boxed().toList();
        steam.expect(times(2), requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/IStoreBrowseService/")))
                .andRespond(withSuccess("{\"response\":{\"store_items\":[{\"appid\":1,\"type\":6}]}}", MediaType.APPLICATION_JSON));

        assertThat(client.findSoftware(appIds)).containsExactly(1);
        steam.verify();
    }

    @Test
    void storeErrorsStopTheSync() {
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/IStoreBrowseService/")))
                .andRespond(withSuccess("{\"response\":{}}", MediaType.APPLICATION_JSON));
        assertThatThrownBy(() -> client.findSoftware(List.of(620)))
                .hasMessage("Steam did not return the store information of the games");

        steam.reset();
        steam.expect(requestTo(org.hamcrest.Matchers.startsWith("https://steam.test/IStoreBrowseService/")))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
        assertThatThrownBy(() -> client.findSoftware(List.of(620)))
                .hasMessageStartingWith("Steam returned HTTP 503 when reading the store information");
    }
}
