package com.bifrost.aplication.mappers;


import com.bifrost.aplication.domain.OutDigitalPlatform;
import com.bifrost.aplication.domain.OutPlatform;
import com.bifrost.aplication.entity.DigitalPlatform;
import com.bifrost.aplication.entity.Platform;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlatformBuilder {

    public List<OutPlatform> convertListPlatform(List<Platform> platforms) {
        return Optional.of(platforms)
                .map(v -> v.stream()
                        .map(OutPlatform::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }


    public List<OutDigitalPlatform> convertListDigitalPlatform(List<DigitalPlatform> digitalPlatforms) {
        return Optional.of(digitalPlatforms)
                .map(v -> v.stream()
                        .map(OutDigitalPlatform::new)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
