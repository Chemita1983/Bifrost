package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.PlatformUseCase;
import com.bifrost.domain.Platform;
import com.bifrost.infrastructure.mapper.WebMapper;
import com.bifrost.infrastructure.web.dto.PlatformRequest;
import com.bifrost.infrastructure.web.dto.PlatformResponse;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class PlatformController {

    private static final String PATH = "platform";

    private final PlatformUseCase platformUseCase;
    private final WebMapper webMapper;
    private final Validator validator;

    public PlatformController(PlatformUseCase platformUseCase, WebMapper webMapper, Validator validator) {
        this.platformUseCase = platformUseCase;
        this.webMapper = webMapper;
        this.validator = validator;
    }

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PlatformResponse> getPlatforms(@RequestParam(value = "name", required = false) String name) {
        List<Platform> platforms = name == null ? platformUseCase.getAll() : platformUseCase.getByName(name);
        return platforms.stream().map(webMapper::toResponse).toList();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformResponse addPlatform(@Valid @RequestBody PlatformRequest request) {
        return webMapper.toResponse(platformUseCase.create(webMapper.toDomain(request)));
    }

    @PatchMapping(value = PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PlatformResponse updatePlatform(@PathVariable("id") Integer id, @RequestBody Map<String, Object> body) {
        PatchRequest.validatePlatform(body, validator);
        return platformUseCase.update(id, current -> webMapper.toDomain(
                        PatchRequest.merge(body, webMapper.toRequest(current))))
                .map(webMapper::toResponse)
                .orElseThrow(PlatformController::notFound);
    }

    @DeleteMapping(PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlatform(@PathVariable("id") Integer id) {
        if (!platformUseCase.delete(id)) {
            throw notFound();
        }
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Platform not found");
    }
}
