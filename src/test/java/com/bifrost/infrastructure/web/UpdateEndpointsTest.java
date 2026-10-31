package com.bifrost.infrastructure.web;

import com.bifrost.application.service.*;
import com.bifrost.infrastructure.mapper.*;
import com.bifrost.infrastructure.persistence.adapter.*;
import com.bifrost.infrastructure.persistence.entity.*;
import com.bifrost.infrastructure.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UpdateEndpointsTest {
    private final PlatformJpa platforms = mock(PlatformJpa.class);
    private final DigitalPlatformJpa stores = mock(DigitalPlatformJpa.class);
    private final VideogamesJpa games = mock(VideogamesJpa.class);
    private final PersistenceMapper persistenceMapper = new PersistenceMapper();
    private final WebMapper webMapper = new WebMapper();
    private final jakarta.validation.Validator validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator();
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new PlatformController(new PlatformApplicationService(new PlatformPersistence(platforms, persistenceMapper)), webMapper, validator),
            new DigitalPlatformController(new DigitalPlatformApplicationService(new DigitalPlatformPersistence(stores, persistenceMapper)), webMapper, validator),
            new VideogameController(new VideogameApplicationService(new VideogamePersistence(games, platforms, stores, persistenceMapper)), webMapper, validator)
    ).setControllerAdvice(new ApiExceptionHandler()).build();

    @Test
    void hoursAreCreatedReadPreservedUpdatedAndCleared() throws Exception {
        when(games.save(any(VideogameEntity.class))).thenAnswer(i -> i.getArgument(0));
        for (String suffix : new String[] {"", ",\"hoursPlayed\":null", ",\"hoursPlayed\":0", ",\"hoursPlayed\":42"}) {
            var result = mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"videogameName\":\"Game\"" + suffix + "}"))
                    .andExpect(status().isOk());
            if (suffix.endsWith(":0")) result.andExpect(jsonPath("$.hoursPlayed").value(0));
            else if (suffix.endsWith(":42")) result.andExpect(jsonPath("$.hoursPlayed").value(42));
            else result.andExpect(jsonPath("$.hoursPlayed").doesNotExist());
        }
        VideogameEntity game = new VideogameEntity();
        game.setId(3); game.setVideogameName("Game"); game.setHoursPlayed(42);
        when(games.findById(3)).thenReturn(Optional.of(game));
        when(games.findAll()).thenReturn(List.of(game));
        mvc.perform(get("/videogames")).andExpect(status().isOk()).andExpect(jsonPath("$[0].hoursPlayed").value(42));
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON).content("{\"videogameName\":\"New\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.hoursPlayed").value(42));
        assertThat(game.getHoursPlayed()).isEqualTo(42);
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON).content("{\"hoursPlayed\":0}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.hoursPlayed").value(0));
        assertThat(game.getHoursPlayed()).isZero();
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON).content("{\"hoursPlayed\":null}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.hoursPlayed").doesNotExist());
        assertThat(game.getHoursPlayed()).isNull();
    }

    @Test
    void rejectsInvalidHoursOnCreateAndPatchBeforePersistence() throws Exception {
        for (String value : new String[] {"-1", "1.5", "\"1\"", "true", "2147483648"}) {
            mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"videogameName\":\"Game\",\"hoursPlayed\":" + value + "}"))
                    .andExpect(status().isBadRequest());
            mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                    .content("{\"hoursPlayed\":" + value + "}"))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(games, platforms, stores);
    }

    @Test
    void videogameFiltersReachMockRepositoryAndReturnArray() throws Exception {
        VideogameEntity game = new VideogameEntity();
        game.setVideogameName("Example");
        when(games.findFiltered(4, 0, 0, true, "rPg")).thenReturn(List.of(game));
        mvc.perform(get("/videogames").param("platformId", "4").param("isDigital", "false")
                        .param("isCompleted", "false").param("multiplayer", "true").param("type", "rPg"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].videogameName").value("Example"));
        verify(games).findFiltered(4, 0, 0, true, "rPg");
        mvc.perform(get("/videogames").param("type", "Shooter"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(games).findFiltered(null, null, null, null, "Shooter");
        mvc.perform(get("/videogames")).andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(games).findAll();
    }

    @Test
    void videogameFiltersRejectInvalidParameters() throws Exception {
        for (String[] parameter : new String[][] {{"platformId", "abc"}, {"platformId", ""},
                {"platformId", "999999999999"}, {"isDigital", "1"}, {"isCompleted", "nope"},
                {"multiplayer", ""}, {"type", " "}}) {
            mvc.perform(get("/videogames").param(parameter[0], parameter[1])).andExpect(status().isBadRequest());
        }
        verifyNoInteractions(games);
    }

    @Test
    void patchesPlatformAndStoreWithoutChangingOmittedFields() throws Exception {
        PlatformEntity platform = new PlatformEntity(); platform.setId(1); platform.setPlatformName("Old");
        platform.setPlatformCompany("Nintendo"); platform.setPlatformYear("2017");
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(2);
        store.setDigitalPlatformName("Old store"); store.setDigitalPlatformCompany("Valve");
        when(platforms.findById(1)).thenReturn(Optional.of(platform));
        when(stores.findById(2)).thenReturn(Optional.of(store));
        mvc.perform(patch("/platform/1").contentType(MediaType.APPLICATION_JSON).content("{\"platformName\":\"Switch\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.platformCompany").value("Nintendo"));
        mvc.perform(patch("/digital-platform/2").contentType(MediaType.APPLICATION_JSON).content("{\"digitalPlatformName\":\"Steam\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.digitalPlatformCompany").value("Valve"));
        assertThat(platform.getPlatformName()).isEqualTo("Switch");
        assertThat(platform.getPlatformYear()).isEqualTo("2017");
        assertThat(store.getDigitalPlatformName()).isEqualTo("Steam");
        verify(platforms).findById(1); verify(stores).findById(2);
    }

    @Test
    void patchPreservesOmittedRelationsAndAcceptsFalseAndZeroThenClearsExplicitNull() throws Exception {
        VideogameEntity game = new VideogameEntity(); game.setId(3); game.setVideogameName("Original");
        game.setIsDigital(1); game.setIsCompleted(1); game.setIsPlatinum(1); game.setMultiplayer(true);
        PlatformEntity platform = new PlatformEntity(); platform.setId(4);
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(5);
        game.setPlatformEntity(platform); game.setDigitalPlatformEntity(store);
        when(games.findById(3)).thenReturn(Optional.of(game));
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"isDigital\":0,\"isCompleted\":0,\"isPlatinum\":0,\"multiplayer\":false}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.videogameName").value("Original"));
        assertThat(game.getIsDigital()).isZero(); assertThat(game.getIsCompleted()).isZero();
        assertThat(game.getIsPlatinum()).isZero(); assertThat(game.isMultiplayer()).isFalse();
        assertThat(game.getPlatformEntity()).isSameAs(platform); assertThat(game.getDigitalPlatformEntity()).isSameAs(store);
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"platformId\":null,\"digitalPlatformId\":null}")) .andExpect(status().isOk());
        assertThat(game.getPlatformEntity()).isNull(); assertThat(game.getDigitalPlatformEntity()).isNull();
        assertThat(game.getVideogameName()).isEqualTo("Original");
        verifyNoInteractions(platforms, stores);
    }

    @Test
    void changedRelationsAreResolvedAndMissingRelationsDoNotMutateGame() throws Exception {
        VideogameEntity game = new VideogameEntity(); game.setId(3); game.setVideogameName("Original");
        PlatformEntity platform = new PlatformEntity(); platform.setId(4); platform.setPlatformName("PC");
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(5); store.setDigitalPlatformName("Steam");
        when(games.findById(3)).thenReturn(Optional.of(game));
        when(platforms.findById(4)).thenReturn(Optional.of(platform)); when(stores.findById(5)).thenReturn(Optional.of(store));
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"platformId\":4,\"digitalPlatformId\":5}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.platform.platformName").value("PC"))
                .andExpect(jsonPath("$.platform.id").value(4))
                .andExpect(jsonPath("$.digitalPlatform.id").value(5));
        assertThat(game.getPlatformEntity()).isSameAs(platform); assertThat(game.getDigitalPlatformEntity()).isSameAs(store);
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"videogameName\":\"Changed\",\"platformId\":99}")) .andExpect(status().isNotFound());
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"videogameName\":\"Changed\",\"digitalPlatformId\":99}")) .andExpect(status().isNotFound());
        assertThat(game.getVideogameName()).isEqualTo("Original");
        assertThat(game.getPlatformEntity()).isSameAs(platform); assertThat(game.getDigitalPlatformEntity()).isSameAs(store);
    }

    @Test
    void nonexistentIdsReturn404AndPutIsNotSupported() throws Exception {
        for (String path : new String[]{"/videogame/99", "/platform/99", "/digital-platform/99"}) {
            mvc.perform(patch(path).contentType(MediaType.APPLICATION_JSON).content("{}")) .andExpect(status().isNotFound());
            mvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content("{}")) .andExpect(status().isMethodNotAllowed());
        }
    }

    @Test
    void invalidValuesAreRejectedBeforePersistence() throws Exception {
        for (String body : new String[]{"{\"platformId\":\"4\"}", "{\"platformId\":4.5}",
                "{\"platformId\":2147483648}", "{\"isDigital\":2}", "{\"isCompleted\":-1}",
                "{\"isPlatinum\":true}", "{\"multiplayer\":1}", "{\"videogameName\":null}",
                "{\"videogameYear\":2024}", "{\"unknown\":1}", "[]", "null"}) {
            mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        }
        for (String body : new String[]{"{\"platformName\":null}", "{\"platformCompany\":true}"})
            mvc.perform(patch("/platform/1").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        for (String body : new String[]{"{\"digitalPlatformCompany\":null}", "{\"digitalPlatformName\":123}"})
            mvc.perform(patch("/digital-platform/2").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        verifyNoInteractions(games, platforms, stores);
    }

    @Test
    void platformAndStoreIdsAreAlsoExposedByGetAndPost() throws Exception {
        PlatformEntity platform = new PlatformEntity(); platform.setId(1); platform.setPlatformName("Switch");
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(2); store.setDigitalPlatformName("Steam");
        when(platforms.findAll()).thenReturn(List.of(platform));
        when(stores.findAll()).thenReturn(List.of(store));
        when(platforms.save(any(PlatformEntity.class))).thenReturn(platform);
        when(stores.save(any(DigitalPlatformEntity.class))).thenReturn(store);

        mvc.perform(get("/platform")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1));
        mvc.perform(get("/digital-platform")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(2));
        mvc.perform(post("/platform").contentType(MediaType.APPLICATION_JSON).content("{\"platformName\":\"Switch\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));
        mvc.perform(post("/digital-platform").contentType(MediaType.APPLICATION_JSON).content("{\"digitalPlatformName\":\"Steam\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void postRemainsUnchanged() throws Exception {
        when(games.save(any(VideogameEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(platforms.save(any(PlatformEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(stores.save(any(DigitalPlatformEntity.class))).thenAnswer(i -> i.getArgument(0));
        mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON).content("{\"videogameName\":\"Game\"}")) .andExpect(status().isOk());
        mvc.perform(post("/platform").contentType(MediaType.APPLICATION_JSON).content("{\"platformName\":\"Switch\"}")) .andExpect(status().isOk());
        mvc.perform(post("/digital-platform").contentType(MediaType.APPLICATION_JSON).content("{\"digitalPlatformName\":\"Steam\"}")) .andExpect(status().isOk());
    }
    @Test
    void deleteEndpointsReturnNoContentOrNotFound() throws Exception {
        when(games.findById(1)).thenReturn(Optional.of(new VideogameEntity()));
        when(platforms.findById(1)).thenReturn(Optional.of(new PlatformEntity()));
        when(stores.findById(1)).thenReturn(Optional.of(new DigitalPlatformEntity()));
        for (String path : new String[]{"/videogame", "/platform", "/digital-platform"}) {
            mvc.perform(delete(path + "/1")).andExpect(status().isNoContent()).andExpect(content().string(""));
            mvc.perform(delete(path + "/99")).andExpect(status().isNotFound());
        }
    }

    @Test
    void deleteReferencedPlatformOrStoreReturnsConflict() throws Exception {
        when(platforms.findById(1)).thenReturn(Optional.of(new PlatformEntity()));
        when(stores.findById(1)).thenReturn(Optional.of(new DigitalPlatformEntity()));
        doThrow(new org.springframework.dao.DataIntegrityViolationException("FK")).when(platforms).delete(any(PlatformEntity.class));
        doThrow(new org.springframework.dao.DataIntegrityViolationException("FK")).when(stores).delete(any(DigitalPlatformEntity.class));
        mvc.perform(delete("/platform/1")).andExpect(status().isConflict());
        mvc.perform(delete("/digital-platform/1")).andExpect(status().isConflict());
    }
    @Test
    void createAndPatchRejectConstraintViolationsBeforePersistence() throws Exception {
        for (String[] request : new String[][] {
                {"/platform", "{}"}, {"/platform", "{\"platformName\":\"   \"}"},
                {"/digital-platform", "{\"digitalPlatformName\":\"\"}"},
                {"/videogame", "{\"videogameName\":\" \"}"},
                {"/videogame", "{\"videogameName\":\"Game\",\"isDigital\":2}"},
                {"/videogame", "{\"videogameName\":\"Game\",\"platformId\":0}"},
                {"/videogame", "{\"videogameName\":\"Game\",\"digitalPlatformId\":-1}"}
        }) {
            mvc.perform(post(request[0]).contentType(MediaType.APPLICATION_JSON).content(request[1]))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").isNotEmpty());
        }
        for (String body : new String[] {"{\"videogameName\":\"  \"}", "{\"platformId\":0}",
                "{\"digitalPlatformId\":-1}", "{\"isPlatinum\":2}"}) {
            mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").isNotEmpty());
        }
        verifyNoInteractions(games, platforms, stores);
    }

    @Test
    void errorResponsesHideUnderlyingDatabaseDetails() throws Exception {
        when(platforms.findById(1)).thenReturn(Optional.of(new PlatformEntity()));
        doThrow(new org.springframework.dao.DataIntegrityViolationException("secret database detail"))
                .when(platforms).delete(any(PlatformEntity.class));
        mvc.perform(delete("/platform/1")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Resource is in use"))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("secret"))));
        mvc.perform(patch("/platform/99").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.message").value("Not Found"));
        mvc.perform(post("/platform").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Invalid request body"));
    }

    @Test
    void createReportsMultipleSortedValidationErrorsAndPatchReportsInvalidField() throws Exception {
        mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"videogameName\":\" \",\"platformId\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("platformId: must be greater than 0; videogameName: must not be blank"));
        mvc.perform(patch("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"multiplayer\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid field: multiplayer"));
        verifyNoInteractions(games, platforms, stores);
    }
}
