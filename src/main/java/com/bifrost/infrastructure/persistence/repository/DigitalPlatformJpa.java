package com.bifrost.infrastructure.persistence.repository;

import com.bifrost.infrastructure.persistence.entity.DigitalPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalPlatformJpa extends JpaRepository<DigitalPlatformEntity, Integer> {
}
