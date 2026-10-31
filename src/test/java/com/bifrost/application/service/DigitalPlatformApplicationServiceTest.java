package com.bifrost.application.service;

import com.bifrost.application.port.out.DigitalPlatformRepositoryPort;
import com.bifrost.application.service.DigitalPlatformApplicationService;
import com.bifrost.domain.DigitalPlatform;
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
class DigitalPlatformApplicationServiceTest {

    @Mock
    private DigitalPlatformRepositoryPort digitalPlatformJpa;

    private DigitalPlatformApplicationService service;

    @BeforeEach
    void setUp() {
        service = new DigitalPlatformApplicationService(digitalPlatformJpa);
    }

    @Test
    void shouldGetAllDigitalPlatforms() {
        List<DigitalPlatform> platforms = List.of(new DigitalPlatform());
        when(digitalPlatformJpa.findAll()).thenReturn(platforms);

        List<DigitalPlatform> result = service.getAll();

        assertThat(result).isSameAs(platforms);
        verify(digitalPlatformJpa).findAll();
    }

    @Test
    void shouldCreateDigitalPlatform() {
        DigitalPlatform platform = new DigitalPlatform();
        when(digitalPlatformJpa.save(platform)).thenReturn(platform);

        DigitalPlatform result = service.create(platform);

        assertThat(result).isSameAs(platform);
        verify(digitalPlatformJpa).save(platform);
    }
    @Test
    void deleteDelegatesAndReturnsRepositoryResult() {
        when(digitalPlatformJpa.delete(1)).thenReturn(true, false);
        assertThat(service.delete(1)).isTrue();
        assertThat(service.delete(1)).isFalse();
        org.mockito.Mockito.verify(digitalPlatformJpa, org.mockito.Mockito.times(2)).delete(1);
    }
}
