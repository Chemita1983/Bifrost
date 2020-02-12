package com.bifrost.aplication.repository;

import com.bifrost.aplication.domain.DigitalPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DigitalPlatformRepository extends JpaRepository<DigitalPlatform, String> {

    @Query(nativeQuery = true, value = "select * from digital_platform")
    List<DigitalPlatform> getDigitalPlatforms();
}
