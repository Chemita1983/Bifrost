package com.bifrost.infrastructure.mapper;

import com.bifrost.domain.DigitalPlatform;
import com.bifrost.domain.Platform;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import org.springframework.stereotype.Component;

@Component
public class PersistenceMapper {

    public Platform toDomain(PlatformEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Platform(entity.getId(), entity.getPlatformName(),
                entity.getPlatformCompany(), entity.getPlatformYear());
    }

    public PlatformEntity toEntity(Platform platform) {
        if (platform == null) {
            return null;
        }
        PlatformEntity entity = new PlatformEntity();
        setId(entity, platform.getId());
        entity.setPlatformName(platform.getName());
        entity.setPlatformCompany(platform.getCompany());
        entity.setPlatformYear(platform.getYear());
        return entity;
    }

    public DigitalPlatform toDomain(DigitalPlatformEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DigitalPlatform(entity.getId(), entity.getDigitalPlatformName(),
                entity.getDigitalPlatformCompany());
    }

    public DigitalPlatformEntity toEntity(DigitalPlatform digitalPlatform) {
        if (digitalPlatform == null) {
            return null;
        }
        DigitalPlatformEntity entity = new DigitalPlatformEntity();
        setId(entity, digitalPlatform.getId());
        entity.setDigitalPlatformName(digitalPlatform.getName());
        entity.setDigitalPlatformCompany(digitalPlatform.getCompany());
        return entity;
    }

    public Videogame toDomain(VideogameEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Videogame(entity.getId(), entity.getVideogameName(), entity.getVideogameType(),
                entity.getVideogameYear(), toDomain(entity.getPlatformEntity()),
                toDomain(entity.getDigitalPlatformEntity()), entity.getCompanyName(),
                entity.getIsDigital(), entity.getIsCompleted(), entity.getIsPlatinum(),
                entity.isMultiplayer());
    }

    public VideogameEntity toEntity(Videogame videogame) {
        if (videogame == null) {
            return null;
        }
        VideogameEntity entity = new VideogameEntity();
        setId(entity, videogame.getId());
        entity.setVideogameName(videogame.getName());
        entity.setVideogameType(videogame.getType());
        entity.setVideogameYear(videogame.getYear());
        entity.setPlatformEntity(toEntity(videogame.getPlatform()));
        entity.setDigitalPlatformEntity(toEntity(videogame.getDigitalPlatform()));
        entity.setCompanyName(videogame.getCompanyName());
        entity.setIsDigital(videogame.getDigital());
        entity.setIsCompleted(videogame.getCompleted());
        entity.setIsPlatinum(videogame.getPlatinum());
        entity.setMultiplayer(videogame.isMultiplayer());
        return entity;
    }

    /** Updates managed entities without replacing their identifiers or managed relations. */
    public void updateEntity(Platform source, PlatformEntity target) {
        target.setPlatformName(source.getName());
        target.setPlatformCompany(source.getCompany());
        target.setPlatformYear(source.getYear());
    }

    public void updateEntity(DigitalPlatform source, DigitalPlatformEntity target) {
        target.setDigitalPlatformName(source.getName());
        target.setDigitalPlatformCompany(source.getCompany());
    }

    public void updateEntity(Videogame source, VideogameEntity target,
                             PlatformEntity platform, DigitalPlatformEntity digitalPlatform) {
        target.setVideogameName(source.getName());
        target.setVideogameType(source.getType());
        target.setVideogameYear(source.getYear());
        target.setCompanyName(source.getCompanyName());
        target.setIsDigital(source.getDigital());
        target.setIsCompleted(source.getCompleted());
        target.setIsPlatinum(source.getPlatinum());
        target.setMultiplayer(source.isMultiplayer());
        target.setPlatformEntity(platform);
        target.setDigitalPlatformEntity(digitalPlatform);
    }

    private void setId(com.bifrost.infrastructure.persistence.entity.BifrostEntity entity, Integer id) {
        if (id != null) {
            entity.setId(id);
        }
    }
}
