package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformControllerTest {

    @Mock
    private PlatformUseCase platformUseCase;

    private PlatformController controller;

    @BeforeEach
    void setUp() {
        controller = new PlatformController(platformUseCase, new WebMapper());
    }

    @Test
    void shouldGetAllPlatformsWithoutName() {
        when(platformUseCase.getAll()).thenReturn(List.of(new Platform()));

        List<PlatformResponse> result = controller.getPlatforms(null);

        assertThat(result).hasSize(1);
        verify(platformUseCase).getAll();
    }

    @Test
    void shouldGetPlatformsByName() {
        when(platformUseCase.getByName("switch")).thenReturn(List.of(new Platform()));

        List<PlatformResponse> result = controller.getPlatforms("switch");

        assertThat(result).hasSize(1);
        verify(platformUseCase).getByName("switch");
    }

    @Test
    void shouldCreatePlatform() {
        Platform platform = new Platform();
        when(platformUseCase.create(any(Platform.class))).thenReturn(platform);

        PlatformResponse result = controller.addPlatform(new PlatformRequest("Switch", "Nintendo", "2017"));

        assertThat(result).isNotNull();
        verify(platformUseCase).create(any(Platform.class));
    }
}
