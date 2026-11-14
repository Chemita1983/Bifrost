package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideogamesJpa extends JpaRepository<VideogameEntity, Integer> {

    @Query("""
            select v from VideogameEntity v
            where (:platformId is null or v.platformEntity.id = :platformId)
              and (:isDigital is null or v.isDigital = :isDigital)
              and (:isCompleted is null or v.isCompleted = :isCompleted)
              and (:multiplayer is null or v.multiplayer = :multiplayer)
              and (:type is null or locate(lower(:type), lower(v.videogameType)) > 0)
            """)
    List<VideogameEntity> findFiltered(@Param("platformId") Integer platformId,
            @Param("isDigital") Integer isDigital, @Param("isCompleted") Integer isCompleted,
            @Param("multiplayer") Boolean multiplayer, @Param("type") String type);

    List<VideogameEntity> findByVideogameNameContaining(String name);

    List<VideogameEntity> findBySteamAppIdNotNull();

    /** Changes only the hours column, so no other data of the game can be overwritten. */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update VideogameEntity v set v.hoursPlayed = :hoursPlayed where v.id = :id")
    int updateHoursPlayed(@Param("id") Integer id, @Param("hoursPlayed") Integer hoursPlayed);
}
