package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.PlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatformJpa extends JpaRepository<PlatformEntity, Integer> {

    List<PlatformEntity> findByPlatformNameContaining(String name);
}
