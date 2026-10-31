package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlatformController.class)
@Import(WebMapper.class)
class PlatformLocaleWebTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PlatformUseCase platformUseCase;

    @Test
    void validationMessagesAreEnglishEvenWhenSpanishIsRequested() throws Exception {
        mvc.perform(post("/platform")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"platformName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("platformName: must not be blank"));

        mvc.perform(post("/platform")
                        .header("Accept-Language", "es")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"platformName\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("platformName: must not be blank"));
    }
}
