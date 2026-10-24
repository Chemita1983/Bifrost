package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.DigitalPlatformUseCase;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import com.bifrost.infrastructure.web.dto.DigitalPlatformResponse;
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
class DigitalPlatformControllerTest {

    @Mock
    private DigitalPlatformUseCase digitalPlatformUseCase;

    private DigitalPlatformController controller;

    @BeforeEach
    void setUp() {
        controller = new DigitalPlatformController(digitalPlatformUseCase, new WebMapper());
    }

    @Test
    void shouldGetAllDigitalPlatforms() {
        when(digitalPlatformUseCase.getAll()).thenReturn(List.of(new DigitalPlatform()));

        List<DigitalPlatformResponse> result = controller.getDigitalPlatforms();

        assertThat(result).hasSize(1);
        verify(digitalPlatformUseCase).getAll();
    }

    @Test
    void shouldCreateDigitalPlatform() {
        DigitalPlatform platform = new DigitalPlatform();
        when(digitalPlatformUseCase.create(any(DigitalPlatform.class))).thenReturn(platform);

        DigitalPlatformResponse result = controller.addDigitalPlatform(
                new DigitalPlatformRequest("Steam", "Valve"));

        assertThat(result).isNotNull();
        verify(digitalPlatformUseCase).create(any(DigitalPlatform.class));
    }
}
