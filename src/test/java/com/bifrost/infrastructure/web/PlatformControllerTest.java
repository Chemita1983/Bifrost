package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformControllerTest {

    @Mock
    private PlatformUseCase platformUseCase;

    private PlatformController controller;

    @BeforeEach
    void setUp() {
        controller = new PlatformController(platformUseCase, new WebMapper(), jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator());
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

    @Test
    void shouldUpdatePlatform() {
        Platform current = new Platform(1, "Switch", "Nintendo", "2017");
        when(platformUseCase.update(eq(1), any())).thenAnswer(invocation -> {
            UnaryOperator<Platform> update = invocation.getArgument(1);
            Platform updated = update.apply(current);
            updated.setId(current.getId());
            return Optional.of(updated);
        });

        PlatformResponse result = controller.updatePlatform(1, Map.of("platformName", "Switch 2"));

        assertThat(result.id()).isEqualTo(1);
        assertThat(result.platformName()).isEqualTo("Switch 2");
        assertThat(result.platformCompany()).isEqualTo("Nintendo");
        verify(platformUseCase).update(eq(1), any());
    }

    @Test
    void shouldSerializePlatformResponseWithIdAndExistingFields() throws Exception {
        PlatformResponse response = PlatformResponse.from(new Platform(7, "Switch", "Nintendo", "2017"));
        ObjectMapper mapper = new ObjectMapper();

        assertThat(response.id()).isEqualTo(7);
        assertThat(mapper.readTree(mapper.writeValueAsBytes(response)))
                .isEqualTo(mapper.readTree("""
                        {"id":7,"platformName":"Switch","platformCompany":"Nintendo","platformYear":"2017"}
                        """));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingPlatform() {
        when(platformUseCase.update(eq(1), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.updatePlatform(1, Map.of("platformName", "Switch 2")))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(exception -> assertThat(((ResponseStatusException) exception).getStatusCode())
                        .isEqualTo(HttpStatus.NOT_FOUND));
        verify(platformUseCase).update(eq(1), any());
    }
    @Test
    void deleteHandlesExistingAndMissingId() {
        when(platformUseCase.delete(1)).thenReturn(true, false);
        controller.deletePlatform(1);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> controller.deletePlatform(1))
                .isInstanceOfSatisfying(org.springframework.web.server.ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.NOT_FOUND));
    }
    @Test
    void deleteTranslatesIntegrityConflict() {
        when(platformUseCase.delete(1)).thenThrow(new org.springframework.dao.DataIntegrityViolationException("constraint"));
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> controller.deletePlatform(1))
                .isInstanceOfSatisfying(org.springframework.web.server.ResponseStatusException.class,
                        ex -> assertThat(ex.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.CONFLICT));
    }
}
