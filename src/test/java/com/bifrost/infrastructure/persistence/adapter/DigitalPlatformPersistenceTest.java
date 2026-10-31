package com.bifrost.infrastructure.persistence.adapter;

import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
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
class DigitalPlatformPersistenceTest {

    @Mock
    private DigitalPlatformJpa digitalPlatformJpa;

    private DigitalPlatformPersistence adapter;

    @BeforeEach
    void setUp() {
        adapter = new DigitalPlatformPersistence(digitalPlatformJpa, new PersistenceMapper());
    }

    @Test
    void shouldFindAllDigitalPlatforms() {
        when(digitalPlatformJpa.findAll()).thenReturn(List.of(new DigitalPlatformEntity()));

        List<DigitalPlatform> result = adapter.findAll();

        assertThat(result).hasSize(1);
        verify(digitalPlatformJpa).findAll();
    }

    @Test
    void shouldSaveDigitalPlatform() {
        when(digitalPlatformJpa.save(org.mockito.ArgumentMatchers.any(DigitalPlatformEntity.class)))
                .thenReturn(new DigitalPlatformEntity());

        DigitalPlatform result = adapter.save(new DigitalPlatform());

        assertThat(result).isNotNull();
        verify(digitalPlatformJpa).save(org.mockito.ArgumentMatchers.any(DigitalPlatformEntity.class));
    }
    @Test
    void deleteReturnsTrueWhenEntityExists() {
        DigitalPlatformEntity entity = new DigitalPlatformEntity();
        when(digitalPlatformJpa.findById(1)).thenReturn(Optional.of(entity));

        assertThat(adapter.delete(1)).isTrue();
        verify(digitalPlatformJpa).delete(entity);
    }

    @Test
    void deleteReturnsFalseWhenEntityIsMissing() {
        when(digitalPlatformJpa.findById(1)).thenReturn(Optional.empty());

        assertThat(adapter.delete(1)).isFalse();
        verify(digitalPlatformJpa, never()).delete(any(DigitalPlatformEntity.class));
    }
}
