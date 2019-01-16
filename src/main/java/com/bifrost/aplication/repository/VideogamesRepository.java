package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.Videogames;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideogamesRepository extends JpaRepository<Videogames,Integer> {

    @Query(nativeQuery = true, value ="select * from videogames")
    List<Videogames> getVideogames();

    @Query(nativeQuery = true, value = "select * from videogames where game_name = :name")
    Optional<Videogames> findVideogameByName(@Param("name") String nameVideogame);
}
