package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import java.util.Map;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.springframework.http.MediaType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PatchMapping;
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
    @PatchMapping(value = PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformResponse updatePlatform(@PathVariable("id") Integer id, @RequestBody Map<String, Object> body) {
        PatchRequest.validate(body, PlatformRequest.class);
        return platformUseCase.update(id, current -> webMapper.toDomain(
                PatchRequest.merge(body, webMapper.toRequest(current), PlatformRequest.class)))
                .map(webMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping(PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlatform(@PathVariable("id") Integer id) {
        try {
            if (!platformUseCase.delete(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Platform not found");
            }
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource is in use", ex);
        }
    }
}
