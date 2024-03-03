package com.bifrost.infrastructure.bbdd;

import com.bifrost.infrastructure.bbdd.entity.VideogameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideogamesRepository extends JpaRepository<VideogameEntity, Integer> {

    @Query(nativeQuery = true, value = "select * from videogames")
    List<VideogameEntity> getVideogames();

    @Query(nativeQuery = true, value = "select * from videogames where name like %:name%")
    List<VideogameEntity> getVideogameByName(@Param("name") String nameVideogame);

}
