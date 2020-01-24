package com.bifrost.aplication.repository;

import com.bifrost.aplication.entity.VideogameConsole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideogamesConsoleRepository extends JpaRepository<VideogameConsole,Integer> {

    @Query(nativeQuery = true, value ="select * from videogames_console")
    List<VideogameConsole> getConsoleVideogames();

    @Query(nativeQuery = true, value = "select * from videogames_console where videogame_name like %:name%")
    List<VideogameConsole> getConsoleVideogameByName(@Param("name") String nameVideogame);

}
