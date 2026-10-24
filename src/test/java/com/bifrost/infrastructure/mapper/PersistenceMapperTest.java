package com.bifrost.infrastructure.mapper;

import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceMapperTest {

    private final PersistenceMapper mapper = new PersistenceMapper();

    @Test
    void shouldMapDigitalPlatformEntityToDomain() {
        DigitalPlatformEntity entity = new DigitalPlatformEntity();
        entity.setId(1);
        entity.setDigitalPlatformName("Steam");
        entity.setDigitalPlatformCompany("Valve");

        DigitalPlatform result = mapper.toDomain(entity);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Steam");
        assertThat(result.getCompany()).isEqualTo("Valve");
    }

    @Test
    void shouldMapDigitalPlatformDomainToEntity() {
        DigitalPlatform platform = new DigitalPlatform(1, "Steam", "Valve");

        DigitalPlatformEntity result = mapper.toEntity(platform);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getDigitalPlatformName()).isEqualTo("Steam");
        assertThat(result.getDigitalPlatformCompany()).isEqualTo("Valve");
    }

}
