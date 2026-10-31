package com.bifrost;

import com.bifrost.infrastructure.persistence.repository.DigitalPlatformJpa;
import com.bifrost.infrastructure.persistence.repository.PlatformJpa;
import com.bifrost.infrastructure.persistence.repository.VideogamesJpa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BifrostApplicationTests {
    @Container
    static final MariaDBContainer<?> DATABASE = new MariaDBContainer<>("mariadb:11.4");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.datasource.url", DATABASE::getJdbcUrl);
        properties.add("spring.datasource.username", DATABASE::getUsername);
        properties.add("spring.datasource.password", DATABASE::getPassword);
        properties.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired VideogamesJpa games;
    @Autowired PlatformJpa platforms;
    @Autowired DigitalPlatformJpa digitalPlatforms;

    @BeforeEach
    void cleanDatabase() {
        games.deleteAll();
        platforms.deleteAll();
        digitalPlatforms.deleteAll();
    }

    private int create(String path, String body) throws Exception {
        String response = mvc.perform(post(path).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return json.readTree(response).get("id").asInt();
    }

    @Test
    void createReadPatchFilterAndDeleteGame() throws Exception {
        int platform = create("/platform", "{\"platformName\":\"Console\"}");
        int digital = create("/digital-platform", "{\"digitalPlatformName\":\"Store\"}");
        int game = create("/videogame", """
                {"videogameName":"First", "videogameType":"RPG", "platformId":%d,
                 "digitalPlatformId":%d, "isDigital":0, "isCompleted":0,
                 "multiplayer":false, "hoursPlayed":0}
                """.formatted(platform, digital));
        create("/videogame", """
                {"videogameName":"Second", "videogameType":"RPG", "isDigital":1,
                 "isCompleted":1, "multiplayer":true, "hoursPlayed":12, "platformId":%d, "digitalPlatformId":%d}
                """.formatted(platform, digital));
        mvc.perform(get("/videogame").param("name", "First"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(game))
                .andExpect(jsonPath("$[0].platform.id").value(platform))
                .andExpect(jsonPath("$[0].digitalPlatform.id").value(digital))
                .andExpect(jsonPath("$[0].hoursPlayed").value(0));
        mvc.perform(get("/videogames").param("platformId", String.valueOf(platform))
                        .param("isDigital", "false").param("isCompleted", "false")
                        .param("multiplayer", "false").param("type", "rpg"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(game));
        mvc.perform(patch("/videogame/{id}", game).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hoursPlayed\":42,\"isCompleted\":1}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.hoursPlayed").value(42))
                .andExpect(jsonPath("$.isCompleted").value(true));
        mvc.perform(get("/videogame").param("name", "First"))
                .andExpect(jsonPath("$[0].hoursPlayed").value(42));
        mvc.perform(delete("/videogame/{id}", game)).andExpect(status().isNoContent());
        mvc.perform(get("/videogame").param("name", "First"))
                .andExpect(jsonPath("$.length()").value(0));
        mvc.perform(delete("/videogame/{id}", game)).andExpect(status().isNotFound());
    }

    @Test
    void referencedPlatformsCannotBeDeletedUntilGameIsRemoved() throws Exception {
        int platform = create("/platform", "{\"platformName\":\"Console\"}");
        int digital = create("/digital-platform", "{\"digitalPlatformName\":\"Store\"}");
        int game = create("/videogame", """
                {"videogameName":"Game", "platformId":%d, "digitalPlatformId":%d}
                """.formatted(platform, digital));
        mvc.perform(delete("/platform/{id}", platform)).andExpect(status().isConflict());
        mvc.perform(delete("/digital-platform/{id}", digital)).andExpect(status().isConflict());
        mvc.perform(delete("/videogame/{id}", game)).andExpect(status().isNoContent());
        mvc.perform(delete("/platform/{id}", platform)).andExpect(status().isNoContent());
        mvc.perform(delete("/digital-platform/{id}", digital)).andExpect(status().isNoContent());
        mvc.perform(delete("/platform/{id}", platform)).andExpect(status().isNotFound());
        mvc.perform(delete("/digital-platform/{id}", digital)).andExpect(status().isNotFound());
    }

    @Test
    void patchPlatformsAndMissingResources() throws Exception {
        int platform = create("/platform", "{\"platformName\":\"Old\"}");
        int digital = create("/digital-platform", "{\"digitalPlatformName\":\"Old store\"}");
        mvc.perform(patch("/platform/{id}", platform).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"platformName\":\"New\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.platformName").value("New"));
        mvc.perform(patch("/digital-platform/{id}", digital).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"digitalPlatformName\":\"New store\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.digitalPlatformName").value("New store"));
        mvc.perform(get("/platform").param("name", "New"))
                .andExpect(jsonPath("$[0].id").value(platform));
        mvc.perform(get("/digital-platform"))
                .andExpect(jsonPath("$[0].digitalPlatformName").value("New store"));
        for (String path : new String[]{"/platform/999999", "/digital-platform/999999", "/videogame/999999"}) {
            mvc.perform(patch(path).contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void invalidHoursAreRejected() throws Exception {
        mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"videogameName\":\"Invalid\",\"hoursPlayed\":-1}"))
                .andExpect(status().isBadRequest());
        int platform = create("/platform", "{\"platformName\":\"Console\"}");
        int id = create("/videogame", "{\"videogameName\":\"Valid\",\"hoursPlayed\":5,\"platformId\":" + platform + "}");
        mvc.perform(patch("/videogame/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"hoursPlayed\":1.5}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/videogame").param("name", "Valid"))
                .andExpect(jsonPath("$[0].hoursPlayed").value(5));
    }
    @Test
    void requiredRelationsAreValidatedWithoutPersistingInvalidRequests() throws Exception {
        int platform = create("/platform", "{\"platformName\":\"Console\"}");
        int store = create("/digital-platform", "{\"digitalPlatformName\":\"Store\"}");
        String[][] invalid = {
                {"{\"videogameName\":\"Missing\"}", "The videogame platform must be specified"},
                {"{\"videogameName\":\"Missing\",\"platformId\":999999}", "The specified platform does not exist"},
                {"{\"videogameName\":\"Missing\",\"platformId\":" + platform + ",\"isDigital\":1}", "The digital platform must be specified for digital videogames"},
                {"{\"videogameName\":\"Missing\",\"platformId\":" + platform + ",\"digitalPlatformId\":999999}", "The specified digital platform does not exist"}
        };
        for (String[] request : invalid) {
            mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON).content(request[0]))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value(request[1]));
            org.assertj.core.api.Assertions.assertThat(games.count()).isZero();
        }
        int id = create("/videogame", "{\"videogameName\":\"Original\",\"platformId\":" + platform + "}");
        String[][] patches = {
                {"{\"platformId\":null,\"videogameName\":\"Changed\"}", "The videogame platform must be specified"},
                {"{\"platformId\":999999}", "The specified platform does not exist"},
                {"{\"isDigital\":1}", "The digital platform must be specified for digital videogames"},
                {"{\"digitalPlatformId\":999999}", "The specified digital platform does not exist"}
        };
        for (String[] request : patches) {
            mvc.perform(patch("/videogame/{id}", id).contentType(MediaType.APPLICATION_JSON).content(request[0]))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value(request[1]));
            mvc.perform(get("/videogame").param("name", "Original"))
                    .andExpect(jsonPath("$[0].platform.id").value(platform))
                    .andExpect(jsonPath("$[0].isDigital").value(false))
                    .andExpect(jsonPath("$[0].digitalPlatform").doesNotExist());
            org.assertj.core.api.Assertions.assertThat(games.count()).isEqualTo(1);
        }
        mvc.perform(patch("/videogame/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isDigital\":1,\"digitalPlatformId\":" + store + "}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.platform.id").value(platform));
        mvc.perform(patch("/videogame/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"digitalPlatformId\":null}"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/videogame").param("name", "Original"))
                .andExpect(jsonPath("$[0].isDigital").value(true))
                .andExpect(jsonPath("$[0].digitalPlatform.id").value(store));
        mvc.perform(patch("/videogame/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isDigital\":0,\"digitalPlatformId\":null}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.digitalPlatform").doesNotExist());
    }

}
