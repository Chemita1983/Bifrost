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
        return new Platform(entity.getId(), entity.getPlatformName(), entity.getPlatformCompany(),
                entity.getPlatformYear());
    }

    public DigitalPlatform toDomain(DigitalPlatformEntity entity) {
        if (entity == null) {
            return null;
        }
        return new DigitalPlatform(entity.getId(), entity.getDigitalPlatformName(), entity.getDigitalPlatformCompany());
    }

    public Videogame toDomain(VideogameEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Videogame(entity.getId(), entity.getVideogameName(), entity.getVideogameType(),
                entity.getVideogameYear(), toDomain(entity.getPlatformEntity()),
                toDomain(entity.getDigitalPlatformEntity()), entity.getCompanyName(), entity.getIsDigital(),
                entity.getIsCompleted(), entity.getIsPlatinum(), entity.isMultiplayer(), entity.getHoursPlayed());
    }

    /*
     * The following methods copy the domain values into a new or managed entity.
     * The entity ID is never changed: Hibernate generates it.
     */

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
        target.setHoursPlayed(source.getHoursPlayed());
        target.setPlatformEntity(platform);
        target.setDigitalPlatformEntity(digitalPlatform);
    }
}
