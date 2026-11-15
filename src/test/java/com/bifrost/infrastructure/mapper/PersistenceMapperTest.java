package com.bifrost.infrastructure.mapper;

import com.bifrost.domain.DigitalPlatform;
import com.bifrost.domain.Platform;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
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
    void nullEntitiesMapToNull() {
        assertThat(mapper.toDomain((PlatformEntity) null)).isNull();
        assertThat(mapper.toDomain((DigitalPlatformEntity) null)).isNull();
        assertThat(mapper.toDomain((VideogameEntity) null)).isNull();
    }

    @Test
    void updatePlatformsCopiesValuesButNeverTheId() {
        PlatformEntity platform = new PlatformEntity();
        platform.setId(7);
        mapper.updateEntity(new Platform(99, "Switch", "Nintendo", "2017"), platform);
        assertThat(platform.getId()).isEqualTo(7);
        assertThat(platform.getPlatformName()).isEqualTo("Switch");
        assertThat(platform.getPlatformCompany()).isEqualTo("Nintendo");
        assertThat(platform.getPlatformYear()).isEqualTo("2017");

        DigitalPlatformEntity store = new DigitalPlatformEntity();
        mapper.updateEntity(new DigitalPlatform(99, "Steam", "Valve"), store);
        assertThat(store.getId()).isNull();
        assertThat(store.getDigitalPlatformName()).isEqualTo("Steam");
        assertThat(store.getDigitalPlatformCompany()).isEqualTo("Valve");
    }

    @Test
    void updateKeepsManagedIdAndUsesResolvedRelations() {
        var target = new VideogameEntity();
        target.setId(7);
        var platform = new PlatformEntity();
        var store = new DigitalPlatformEntity();
        var source = new Videogame();
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
    void mapsCoverUrlBetweenEntityAndDomain() {
        var entity = new VideogameEntity();
        entity.setCoverUrl("https://example.com/cover.jpg");
        Videogame domain = mapper.toDomain(entity);
        assertThat(domain.getCoverUrl()).isEqualTo("https://example.com/cover.jpg");

        var target = new VideogameEntity();
        mapper.updateEntity(domain, target, null, null);
        assertThat(target.getCoverUrl()).isEqualTo("https://example.com/cover.jpg");
        domain.setCoverUrl(null);
        mapper.updateEntity(domain, target, null, null);
        assertThat(target.getCoverUrl()).isNull();
    }

    @Test
    void mapsSteamAppIdToDomainButUpdateEntityDoesNotCopyIt() {
        var entity = new VideogameEntity();
        entity.setSteamAppId(620);
        Videogame domain = mapper.toDomain(entity);
        assertThat(domain.getSteamAppId()).isEqualTo(620);

        domain.setSteamAppId(1);
        mapper.updateEntity(domain, entity, null, null);
        assertThat(entity.getSteamAppId()).isEqualTo(620);
    }

    @Test
    void mapsVideogameHoursInBothDirections() {
        for (Integer hours : new Integer[] {null, 0, 42}) {
            var entity = new VideogameEntity();
            entity.setHoursPlayed(hours);
            var domain = mapper.toDomain(entity);
            assertThat(domain.getHoursPlayed()).isEqualTo(hours);
            var copy = new VideogameEntity();
            mapper.updateEntity(domain, copy, null, null);
            assertThat(copy.getHoursPlayed()).isEqualTo(hours);
        }
    }
}
