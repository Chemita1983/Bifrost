package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideogamesJpa extends JpaRepository<VideogameEntity, Integer> {

    List<VideogameEntity> findByVideogameNameContaining(String name);
    @Modifying
    @Query("delete from VideogameEntity e where e.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Integer id);
}
