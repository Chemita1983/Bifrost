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
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new PlatformController(new PlatformApplicationService(new PlatformPersistence(platforms, persistenceMapper)), webMapper),
            new DigitalPlatformController(new DigitalPlatformApplicationService(new DigitalPlatformPersistence(stores, persistenceMapper)), webMapper),
            new VideogameController(new VideogameApplicationService(new VideogamePersistence(games, platforms, stores, persistenceMapper)), webMapper)
    ).build();

    @Test
    void patchesPlatformAndStoreWithoutChangingOmittedFields() throws Exception {
        PlatformEntity platform = new PlatformEntity(); platform.setId(1); platform.setPlatformName("Old");
        platform.setPlatformCompany("Nintendo"); platform.setPlatformYear("2017");
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(2);
        store.setDigitalPlatformName("Old store"); store.setDigitalPlatformCompany("Valve");
        when(platforms.findById(1)).thenReturn(Optional.of(platform));
        when(stores.findById(2)).thenReturn(Optional.of(store));
        mvc.perform(patch("/platform/1").contentType(MediaType.APPLICATION_JSON).content("{\"platformName\":\"Switch\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.platformCompany").value("Nintendo"));
        mvc.perform(patch("/digital-platform/2").contentType(MediaType.APPLICATION_JSON).content("{\"digitalPlatformName\":\"Steam\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.digitalPlatformCompany").value("Valve"));
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
                .andExpect(status().isOk()).andExpect(jsonPath("$.platform.platformName").value("PC"));
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
    void postRemainsUnchanged() throws Exception {
        when(games.save(any(VideogameEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(platforms.save(any(PlatformEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(stores.save(any(DigitalPlatformEntity.class))).thenAnswer(i -> i.getArgument(0));
        mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON).content("{\"videogameName\":\"Game\"}")) .andExpect(status().isOk());
        mvc.perform(post("/platform").contentType(MediaType.APPLICATION_JSON).content("{\"platformName\":\"Switch\"}")) .andExpect(status().isOk());
        mvc.perform(post("/digital-platform").contentType(MediaType.APPLICATION_JSON).content("{\"digitalPlatformName\":\"Steam\"}")) .andExpect(status().isOk());
    }
}
