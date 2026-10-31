package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformPersistenceTest {

    @Mock
    private PlatformJpa platformRepository;

    private PlatformPersistence adapter;

    @BeforeEach
    void setUp() {
        adapter = new PlatformPersistence(platformRepository, new PersistenceMapper());
    }

    @Test
    void shouldFindAllPlatforms() {
        when(platformRepository.findAll()).thenReturn(List.of(new PlatformEntity()));

        List<Platform> result = adapter.findAll();

        assertThat(result).hasSize(1);
        verify(platformRepository).findAll();
    }

    @Test
    void shouldFindPlatformsByName() {
        when(platformRepository.findByPlatformNameContaining("xbox"))
                .thenReturn(List.of(new PlatformEntity()));

        List<Platform> result = adapter.findByNameContaining("xbox");

        assertThat(result).hasSize(1);
        verify(platformRepository).findByPlatformNameContaining("xbox");
    }

    @Test
    void shouldSavePlatform() {
        when(platformRepository.save(org.mockito.ArgumentMatchers.any(PlatformEntity.class)))
                .thenReturn(new PlatformEntity());

        Platform result = adapter.save(new Platform());

        assertThat(result).isNotNull();
        verify(platformRepository).save(org.mockito.ArgumentMatchers.any(PlatformEntity.class));
    }
    @Test
    void deleteReturnsTrueWhenEntityExists() {
        PlatformEntity entity = new PlatformEntity();
        when(platformRepository.findById(1)).thenReturn(Optional.of(entity));

        assertThat(adapter.delete(1)).isTrue();
        verify(platformRepository).delete(entity);
    }

    @Test
    void deleteReturnsFalseWhenEntityIsMissing() {
        when(platformRepository.findById(1)).thenReturn(Optional.empty());

        assertThat(adapter.delete(1)).isFalse();
        verify(platformRepository, never()).delete(any(PlatformEntity.class));
    }
}
