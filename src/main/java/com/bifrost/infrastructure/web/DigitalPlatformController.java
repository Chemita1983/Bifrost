package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.DigitalPlatformUseCase;
import com.bifrost.domain.DigitalPlatform;
import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import java.util.Map;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import com.bifrost.infrastructure.web.dto.DigitalPlatformResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DigitalPlatformController {

    private static final String PATH = "digital-platform";

    private final DigitalPlatformUseCase digitalPlatformUseCase;
    private final WebMapper webMapper;
    private final Validator validator;

    public DigitalPlatformController(DigitalPlatformUseCase digitalPlatformUseCase, WebMapper webMapper, Validator validator) {
        this.digitalPlatformUseCase = digitalPlatformUseCase;
        this.webMapper = webMapper;
        this.validator = validator;
    }

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DigitalPlatformResponse> getDigitalPlatforms() {
        return digitalPlatformUseCase.getAll().stream().map(webMapper::toResponse).toList();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DigitalPlatformResponse addDigitalPlatform(@Valid @RequestBody DigitalPlatformRequest request) {
        DigitalPlatform platform = webMapper.toDomain(request);
        return webMapper.toResponse(digitalPlatformUseCase.create(platform));
    }
    @PatchMapping(value = PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DigitalPlatformResponse updateDigitalPlatform(@PathVariable("id") Integer id, @RequestBody Map<String, Object> body) {
        PatchRequest.validateDigitalPlatform(body, validator);
        return digitalPlatformUseCase.update(id, current -> webMapper.toDomain(
                PatchRequest.merge(body, webMapper.toRequest(current))))
                .map(webMapper::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping(PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDigitalPlatform(@PathVariable("id") Integer id) {
        try {
            if (!digitalPlatformUseCase.delete(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DigitalPlatform not found");
            }
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Resource is in use", ex);
        }
    }
}
