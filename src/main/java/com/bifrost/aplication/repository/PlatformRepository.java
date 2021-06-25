package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform,Integer> {

    @Query(nativeQuery = true, value = "select * from platform")
    List<Platform> getPlatforms();

    @Query(nativeQuery = true, value = "select * from videogames where videogame_name = :name")
    List<Platform> getPlatformByName(@Param("name") String name);
}
