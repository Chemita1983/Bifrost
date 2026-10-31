package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformUpdateRequest;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlatformController {

    private static final String PATH = "platform";

    private final PlatformUseCase platformUseCase;
    private final WebMapper webMapper;

    public PlatformController(PlatformUseCase platformUseCase, WebMapper webMapper) {
        this.platformUseCase = platformUseCase;
        this.webMapper = webMapper;
    }

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlatformResponse> getPlatforms(@RequestParam(value = "name", required = false) String name) {
        List<Platform> platforms = name == null ? platformUseCase.getAll() : platformUseCase.getByName(name);
        return platforms.stream().map(webMapper::toResponse).toList();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformResponse addPlatform(@RequestBody PlatformRequest request) {
        Platform platform = webMapper.toDomain(request);
        return webMapper.toResponse(platformUseCase.create(platform));
    }
    @PutMapping(value = PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformResponse updatePlatform(@PathVariable("id") Integer id, @RequestBody PlatformUpdateRequest request) {
        return platformUseCase.update(id, webMapper.toDomain(request.toRequest()))
                .map(webMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
