package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlatformJpa extends JpaRepository<PlatformEntity, Integer> {

    List<PlatformEntity> findByPlatformNameContaining(String name);
    @Modifying
    @Query("delete from PlatformEntity e where e.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Integer id);
}
