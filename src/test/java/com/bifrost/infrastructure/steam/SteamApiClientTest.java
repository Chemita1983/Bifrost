package com.bifrost.infrastructure.steam;

import com.bifrost.application.port.out.SteamLibraryPort.OwnedGame;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
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
                .hasMessage("Could not read the Steam library")
                .hasNoCause();
    }
}
