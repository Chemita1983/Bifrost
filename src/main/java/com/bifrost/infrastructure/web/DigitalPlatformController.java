package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.DigitalPlatformUseCase;
import com.bifrost.infrastructure.mapper.WebMapper;
import com.bifrost.infrastructure.web.dto.DigitalPlatformRequest;
import com.bifrost.infrastructure.web.dto.DigitalPlatformResponse;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class DigitalPlatformController {

    private static final String PATH = "digital-platform";

    private final DigitalPlatformUseCase digitalPlatformUseCase;
    private final WebMapper webMapper;
    private final Validator validator;

    public DigitalPlatformController(DigitalPlatformUseCase digitalPlatformUseCase, WebMapper webMapper,
                                     Validator validator) {
        this.digitalPlatformUseCase = digitalPlatformUseCase;
        this.webMapper = webMapper;
        this.validator = validator;
    }

    @GetMapping(value = PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DigitalPlatformResponse> getDigitalPlatforms() {
        return digitalPlatformUseCase.getAll().stream().map(webMapper::toResponse).toList();
    }

    @PostMapping(value = PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DigitalPlatformResponse addDigitalPlatform(@Valid @RequestBody DigitalPlatformRequest request) {
        return webMapper.toResponse(digitalPlatformUseCase.create(webMapper.toDomain(request)));
    }

    @PatchMapping(value = PATH + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DigitalPlatformResponse updateDigitalPlatform(@PathVariable("id") Integer id,
                                                         @RequestBody Map<String, Object> body) {
        PatchRequest.validateDigitalPlatform(body, validator);
        return digitalPlatformUseCase.update(id, current -> webMapper.toDomain(
                        PatchRequest.merge(body, webMapper.toRequest(current))))
                .map(webMapper::toResponse)
                .orElseThrow(DigitalPlatformController::notFound);
    }

    @DeleteMapping(PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDigitalPlatform(@PathVariable("id") Integer id) {
        if (!digitalPlatformUseCase.delete(id)) {
            throw notFound();
        }
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Digital platform not found");
    }
}
