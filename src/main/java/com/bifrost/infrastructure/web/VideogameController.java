package com.bifrost.infrastructure.web;

import com.bifrost.application.port.in.VideogameUseCase;
import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import com.bifrost.infrastructure.web.dto.VideogameResponse;
import com.bifrost.infrastructure.mapper.WebMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideogameController {

	private static final String PATH_VIDEOGAME = "videogame";
	private static final String PATH_VIDEOGAMES = "videogames";

    private final VideogameUseCase videogameUseCase;
    private final WebMapper webMapper;

    public VideogameController(VideogameUseCase videogameUseCase, WebMapper webMapper) {
        this.videogameUseCase = videogameUseCase;
        this.webMapper = webMapper;
	}

	@GetMapping(value = PATH_VIDEOGAME, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VideogameResponse> getVideogameByName(@RequestParam("name") String name) {
        return videogameUseCase.getByName(name).stream().map(webMapper::toResponse).toList();
	}

	@GetMapping(value = PATH_VIDEOGAMES, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VideogameResponse> getVideogames() {
        return videogameUseCase.getAll().stream().map(webMapper::toResponse).toList();
	}

	@PostMapping(value = PATH_VIDEOGAME, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public VideogameResponse addVideogame(@RequestBody VideogameRequest request) {
        Videogame videogame = webMapper.toDomain(request);
        return webMapper.toResponse(videogameUseCase.create(videogame));
	}
}
