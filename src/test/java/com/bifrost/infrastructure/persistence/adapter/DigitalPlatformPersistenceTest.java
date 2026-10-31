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

import static org.assertj.core.api.Assertions.assertThat;
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
    void deleteReturnsWhetherRowWasAffected() {
        when(digitalPlatformJpa.deleteByIdAndReturnCount(1)).thenReturn(1, 0);
        assertThat(adapter.delete(1)).isTrue();
        assertThat(adapter.delete(1)).isFalse();
        org.mockito.Mockito.verify(digitalPlatformJpa, org.mockito.Mockito.times(2)).deleteByIdAndReturnCount(1);
    }
}
