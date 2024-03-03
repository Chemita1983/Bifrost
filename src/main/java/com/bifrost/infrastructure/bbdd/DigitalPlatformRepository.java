package com.bifrost.infrastructure.bbdd;

import com.bifrost.infrastructure.bbdd.entity.DigitalPlatformEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DigitalPlatformRepository extends JpaRepository<DigitalPlatformEntity, String> {

    @Query(nativeQuery = true, value = "select * from digital_platform")
    List<DigitalPlatformEntity> getDigitalPlatforms();
}
