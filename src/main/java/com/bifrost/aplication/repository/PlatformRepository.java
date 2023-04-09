package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<PlatformEntity, Integer> {

    @Query(nativeQuery = true, value = "select * from platform")
    List<PlatformEntity> getPlatforms();

    @Query(nativeQuery = true, value = "select * from videogames where videogame_name = :name")
    List<PlatformEntity> getPlatformByName(@Param("name") String name);
}
