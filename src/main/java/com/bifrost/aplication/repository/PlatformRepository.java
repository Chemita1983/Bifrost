package com.bifrost.aplication.repository;

import com.bifrost.aplication.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform,String> {

    @Query(nativeQuery = true, value = "select * from platform")
    List<Platform> getPlatforms();
}
