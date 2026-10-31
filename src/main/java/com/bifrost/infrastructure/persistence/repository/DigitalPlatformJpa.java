package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DigitalPlatformJpa extends JpaRepository<DigitalPlatformEntity, Integer> {
    @Modifying
    @Query("delete from DigitalPlatformEntity e where e.id = :id")
    int deleteByIdAndReturnCount(@Param("id") Integer id);
}
