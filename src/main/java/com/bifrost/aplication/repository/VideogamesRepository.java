package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.Videogame;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideogamesRepository extends JpaRepository<Videogame,Integer> {

    @Query(nativeQuery = true, value ="select * from videogame")
    List<Videogame> getVideogames();

    @Query(nativeQuery = true, value = "select * from videogame where videogame_name like %:name%")
    List<Videogame> findVideogameByName(@Param("name") String nameVideogame);
}