package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.VideogameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideogamesJpa extends JpaRepository<VideogameEntity, Integer> {

    List<VideogameEntity> findByVideogameNameContaining(String name);
}
