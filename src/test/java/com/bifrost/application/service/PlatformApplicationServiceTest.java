package com.bifrost.application.service;

import com.bifrost.application.port.out.PlatformRepositoryPort;
import com.bifrost.application.service.PlatformApplicationService;
import com.bifrost.domain.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformApplicationServiceTest {

    @Mock
    private PlatformRepositoryPort platformRepository;

    private PlatformApplicationService service;

    @BeforeEach
    void setUp() {
        service = new PlatformApplicationService(platformRepository);
    }

    @Test
    void shouldGetAllPlatforms() {
        List<Platform> platforms = List.of(new Platform());
        when(platformRepository.findAll()).thenReturn(platforms);

        List<Platform> result = service.getAll();

        assertThat(result).isSameAs(platforms);
        verify(platformRepository).findAll();
    }

    @Test
    void shouldGetPlatformsByName() {
        List<Platform> platforms = List.of(new Platform());
        when(platformRepository.findByNameContaining("playstation")).thenReturn(platforms);

        List<Platform> result = service.getByName("playstation");

        assertThat(result).isSameAs(platforms);
        verify(platformRepository).findByNameContaining("playstation");
    }

    @Test
    void shouldCreatePlatform() {
        Platform platform = new Platform();
        when(platformRepository.save(platform)).thenReturn(platform);

        Platform result = service.create(platform);

        assertThat(result).isSameAs(platform);
        verify(platformRepository).save(platform);
    }
    @Test
    void deleteDelegatesAndReturnsRepositoryResult() {
        when(platformRepository.delete(1)).thenReturn(true, false);
        assertThat(service.delete(1)).isTrue();
        assertThat(service.delete(1)).isFalse();
        org.mockito.Mockito.verify(platformRepository, org.mockito.Mockito.times(2)).delete(1);
    }
}
