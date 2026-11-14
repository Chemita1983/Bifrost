package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.VideogameUseCase;
import com.bifrost.infrastructure.mapper.WebMapper;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import com.bifrost.infrastructure.web.dto.VideogameResponse;
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
public class VideogameController {

    private static final String PATH_VIDEOGAME = "videogame";
    private static final String PATH_VIDEOGAMES = "videogames";

    private final VideogameUseCase videogameUseCase;
    private final WebMapper webMapper;
    private final Validator validator;

    public VideogameController(VideogameUseCase videogameUseCase, WebMapper webMapper, Validator validator) {
        this.videogameUseCase = videogameUseCase;
        this.webMapper = webMapper;
        this.validator = validator;
    }

    @GetMapping(value = PATH_VIDEOGAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VideogameResponse> getVideogameByName(@RequestParam("name") String name) {
        return videogameUseCase.getByName(name).stream().map(webMapper::toResponse).toList();
    }

    /**
     * Filters are optional and combinable. They are received as text so that invalid values
     * (for example {@code isDigital=1}) return a clear 400 message.
     */
    @GetMapping(value = PATH_VIDEOGAMES, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VideogameResponse> getVideogames(
            @RequestParam(value = "platformId", required = false) String platformId,
            @RequestParam(value = "isDigital", required = false) String isDigital,
            @RequestParam(value = "isCompleted", required = false) String isCompleted,
            @RequestParam(value = "multiplayer", required = false) String multiplayer,
            @RequestParam(value = "type", required = false) String type) {
        Integer platform = parsePlatformId(platformId);
        Boolean digital = parseBoolean(isDigital, "isDigital");
        Boolean completed = parseBoolean(isCompleted, "isCompleted");
        Boolean multi = parseBoolean(multiplayer, "multiplayer");
        if (type != null && type.isBlank()) {
            throw badRequest("type");
        }
        return videogameUseCase.getFiltered(platform, digital, completed, multi, type).stream()
                .map(webMapper::toResponse).toList();
    }

    @PostMapping(value = PATH_VIDEOGAME, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public VideogameResponse addVideogame(@Valid @RequestBody VideogameRequest request) {
        return webMapper.toResponse(videogameUseCase.create(webMapper.toDomain(request)));
    }

    @PatchMapping(value = PATH_VIDEOGAME + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public VideogameResponse updateVideogame(@PathVariable("id") Integer id, @RequestBody Map<String, Object> body) {
        PatchRequest.validateVideogame(body, validator);
        return videogameUseCase.update(id, current -> webMapper.toDomain(
                        PatchRequest.merge(body, webMapper.toRequest(current))))
                .map(webMapper::toResponse)
                .orElseThrow(VideogameController::notFound);
    }

    @DeleteMapping(PATH_VIDEOGAME + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVideogame(@PathVariable("id") Integer id) {
        if (!videogameUseCase.delete(id)) {
            throw notFound();
        }
    }

    private static Integer parsePlatformId(String value) {
        if (value == null) return null;
        try {
            int id = Integer.parseInt(value);
            if (id > 0) return id;
        } catch (NumberFormatException ignored) {
            // Handled below with the same message as a non-positive ID.
        }
        throw badRequest("platformId");
    }

    private static Boolean parseBoolean(String value, String name) {
        if (value == null) return null;
        if ("true".equalsIgnoreCase(value)) return true;
        if ("false".equalsIgnoreCase(value)) return false;
        throw badRequest(name);
    }

    private static ResponseStatusException badRequest(String parameter) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid " + parameter);
    }

    private static ResponseStatusException notFound() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Videogame not found");
    }
}
