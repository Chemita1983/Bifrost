package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.VideogamePc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideogamesPcRepository extends JpaRepository<VideogamePc, Integer> {

    @Query(nativeQuery = true, value = "select * from videogames_pc")
    List<VideogamePc> getPcVideogames();

    @Query(nativeQuery = true, value = "select * from videogames_pc where videogame_pc_name like %:name%")
    List<VideogamePc> getPcVideogameByName(@Param("name") String nameVideogame);

}