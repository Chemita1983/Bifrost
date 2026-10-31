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

    @Test
    void updateKeepsManagedIdAndUsesResolvedRelations() {
        var target = new com.bifrost.infrastructure.persistence.entity.VideogameEntity();
        target.setId(7);
        var platform = new com.bifrost.infrastructure.persistence.entity.PlatformEntity();
        var store = new DigitalPlatformEntity();
        var source = new com.bifrost.domain.Videogame();
        source.setId(99);
        source.setName("Updated");
        source.setHoursPlayed(0);
        mapper.updateEntity(source, target, platform, store);
        assertThat(target.getId()).isEqualTo(7);
        assertThat(target.getVideogameName()).isEqualTo("Updated");
        assertThat(target.getHoursPlayed()).isZero();
        assertThat(target.getPlatformEntity()).isSameAs(platform);
        assertThat(target.getDigitalPlatformEntity()).isSameAs(store);
        source.setHoursPlayed(null);
        mapper.updateEntity(source, target, null, null);
        assertThat(target.getHoursPlayed()).isNull();
        assertThat(target.getPlatformEntity()).isNull();
        assertThat(target.getDigitalPlatformEntity()).isNull();
    }

    @Test
    void mapsVideogameHoursInBothDirections() {
        var entity = new com.bifrost.infrastructure.persistence.entity.VideogameEntity();
        for (Integer hours : new Integer[] {null, 0, 42}) {
            entity.setHoursPlayed(hours);
            var domain = mapper.toDomain(entity);
            assertThat(domain.getHoursPlayed()).isEqualTo(hours);
            assertThat(mapper.toEntity(domain).getHoursPlayed()).isEqualTo(hours);
        }
    }

}
