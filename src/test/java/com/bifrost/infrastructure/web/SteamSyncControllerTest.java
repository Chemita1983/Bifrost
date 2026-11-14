package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.SteamSyncUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SteamSyncControllerTest {

    private final SteamSyncUseCase useCase = mock(SteamSyncUseCase.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(new SteamSyncController(useCase))
            .setControllerAdvice(new ApiExceptionHandler()).build();

    @Test
    void syncReturnsTheSummaryAndSavesByDefault() throws Exception {
        when(useCase.sync(false)).thenReturn(new SteamSyncUseCase.Result(3, 2, 1, 4, false));

        mvc.perform(post("/steam/sync"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"created\":3,\"updated\":2,\"unchanged\":1,\"skipped\":4,\"dryRun\":false}", true));
        verify(useCase).sync(false);
    }

    @Test
    void dryRunIsPassedToTheUseCase() throws Exception {
        when(useCase.sync(true)).thenReturn(new SteamSyncUseCase.Result(3, 0, 0, 0, true));

        mvc.perform(post("/steam/sync").param("dryRun", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"created\":3,\"dryRun\":true}"));
        verify(useCase).sync(true);
    }

    @Test
    void invalidDryRunAndGetAreRejectedWithoutSyncing() throws Exception {
        mvc.perform(post("/steam/sync").param("dryRun", "maybe")).andExpect(status().isBadRequest());
        mvc.perform(get("/steam/sync")).andExpect(status().isMethodNotAllowed());
        verifyNoInteractions(useCase);
    }

    @Test
    void steamProblemsReturnAGenericServerError() throws Exception {
        when(useCase.sync(false)).thenThrow(new IllegalStateException("Configure STEAM_API_KEY and STEAM_ID"));

        mvc.perform(post("/steam/sync"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{\"message\":\"Internal server error\"}", true));
    }
}
