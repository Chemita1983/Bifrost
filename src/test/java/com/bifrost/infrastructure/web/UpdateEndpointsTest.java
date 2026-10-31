package com.bifrost.infrastructure.web;

import com.bifrost.application.service.*;
import com.bifrost.infrastructure.mapper.PersistenceMapper;
import com.bifrost.infrastructure.mapper.WebMapper;
import com.bifrost.infrastructure.persistence.adapter.*;
import com.bifrost.infrastructure.persistence.entity.*;
import com.bifrost.infrastructure.persistence.repository.*;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void platformUpdatesExistingEntityAndReturns404ForMissingId() throws Exception {
        PlatformEntity entity = new PlatformEntity();
        entity.setId(1);
        when(platforms.findById(1)).thenReturn(Optional.of(entity));
        String body = "{\"platformName\":\"Switch\",\"platformCompany\":\"Nintendo\",\"platformYear\":\"2017\"}";
        mvc.perform(put("/platform/1").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.platformName").value("Switch"))
                .andExpect(jsonPath("$.id").doesNotExist());
        assertThat(entity.getPlatformCompany()).isEqualTo("Nintendo");
        assertThat(entity.getPlatformYear()).isEqualTo("2017");
        mvc.perform(put("/platform/99").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound());
        verify(platforms, never()).save(any());
    }

    @Test
    void storeUpdatesExistingEntityAndReturns404ForMissingId() throws Exception {
        DigitalPlatformEntity entity = new DigitalPlatformEntity();
        entity.setId(2);
        when(stores.findById(2)).thenReturn(Optional.of(entity));
        String body = "{\"digitalPlatformName\":\"Steam\",\"digitalPlatformCompany\":\"Valve\"}";
        mvc.perform(put("/digital-platform/2").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.digitalPlatformName").value("Steam"))
                .andExpect(jsonPath("$.id").doesNotExist());
        assertThat(entity.getDigitalPlatformCompany()).isEqualTo("Valve");
        mvc.perform(put("/digital-platform/99").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound());
        verify(stores, never()).save(any());
    }

    @Test
    void videogameUpdatesFieldsAndRelationsAndReturns404ForMissingId() throws Exception {
        VideogameEntity entity = new VideogameEntity();
        entity.setId(3);
        PlatformEntity platform = new PlatformEntity(); platform.setId(4); platform.setPlatformName("PC");
        DigitalPlatformEntity store = new DigitalPlatformEntity(); store.setId(5); store.setDigitalPlatformName("Steam");
        when(games.findById(3)).thenReturn(Optional.of(entity));
        when(platforms.getReferenceById(4)).thenReturn(platform);
        when(stores.getReferenceById(5)).thenReturn(store);
        String body = "{\"videogameName\":\"Game\",\"videogameType\":\"RPG\",\"videogameYear\":\"2024\",\"platformId\":4,\"digitalPlatformId\":5,\"companyName\":\"Studio\",\"isDigital\":1,\"isCompleted\":1,\"isPlatinum\":1,\"multiplayer\":true}";
        mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.platform.platformName").value("PC"))
                .andExpect(jsonPath("$.digitalPlatform.digitalPlatformName").value("Steam"));
        assertThat(entity.getVideogameType()).isEqualTo("RPG");
        assertThat(entity.getVideogameYear()).isEqualTo("2024");
        assertThat(entity.getCompanyName()).isEqualTo("Studio");
        assertThat(entity.getIsDigital()).isEqualTo(1);
        assertThat(entity.getIsCompleted()).isEqualTo(1);
        assertThat(entity.getIsPlatinum()).isEqualTo(1);
        assertThat(entity.isMultiplayer()).isTrue();
        assertThat(entity.getPlatformEntity()).isSameAs(platform);
        assertThat(entity.getDigitalPlatformEntity()).isSameAs(store);
        mvc.perform(put("/videogame/99").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound());
        verify(games, never()).save(any());
        mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON)
                .content(body.replace("\"platformId\":4", "\"platformId\":null")
                        .replace("\"digitalPlatformId\":5", "\"digitalPlatformId\":null")))
                .andExpect(status().isOk());
        assertThat(entity.getPlatformEntity()).isNull();
        assertThat(entity.getDigitalPlatformEntity()).isNull();
    }

    @Test
    void incompleteVideogameNeverTouchesPersistence() throws Exception {
        VideogameEntity existing = new VideogameEntity();
        existing.setVideogameName("Original");
        existing.setIsDigital(1);
        existing.setMultiplayer(true);
        PlatformEntity platform = new PlatformEntity();
        DigitalPlatformEntity store = new DigitalPlatformEntity();
        existing.setPlatformEntity(platform);
        existing.setDigitalPlatformEntity(store);
        ObjectMapper mapper = new ObjectMapper();
        String complete = "{\"videogameName\":\"Game\",\"videogameType\":\"RPG\",\"videogameYear\":\"2024\",\"platformId\":4,\"digitalPlatformId\":5,\"companyName\":\"Studio\",\"isDigital\":1,\"isCompleted\":1,\"isPlatinum\":1,\"multiplayer\":true}";
        for (String field : new String[]{"videogameName", "videogameType", "videogameYear", "platformId",
                "digitalPlatformId", "companyName", "isDigital", "isCompleted", "isPlatinum", "multiplayer"}) {
            ObjectNode body = (ObjectNode) mapper.readTree(complete);
            body.remove(field);
            mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(body.toString()))
                    .andExpect(status().isBadRequest());
        }
        ObjectNode nullFlag = (ObjectNode) mapper.readTree(complete);
        nullFlag.putNull("isDigital");
        mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(nullFlag.toString()))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON).content("{\"videogameName\":\"Other\"}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(games, platforms, stores);
        assertThat(existing.getVideogameName()).isEqualTo("Original");
        assertThat(existing.getIsDigital()).isEqualTo(1);
        assertThat(existing.isMultiplayer()).isTrue();
        assertThat(existing.getPlatformEntity()).isSameAs(platform);
        assertThat(existing.getDigitalPlatformEntity()).isSameAs(store);
    }

    @Test
    void incompletePlatformAndStoreNeverTouchPersistence() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String platformBody = "{\"platformName\":\"Switch\",\"platformCompany\":\"Nintendo\",\"platformYear\":\"2017\"}";
        for (String field : new String[]{"platformName", "platformCompany", "platformYear"}) {
            ObjectNode body = (ObjectNode) mapper.readTree(platformBody);
            body.remove(field);
            mvc.perform(put("/platform/1").contentType(MediaType.APPLICATION_JSON).content(body.toString()))
                    .andExpect(status().isBadRequest());
        }
        String storeBody = "{\"digitalPlatformName\":\"Steam\",\"digitalPlatformCompany\":\"Valve\"}";
        for (String field : new String[]{"digitalPlatformName", "digitalPlatformCompany"}) {
            ObjectNode body = (ObjectNode) mapper.readTree(storeBody);
            body.remove(field);
            mvc.perform(put("/digital-platform/2").contentType(MediaType.APPLICATION_JSON).content(body.toString()))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(platforms, stores, games);
    }

    @Test
    void postStillAcceptsPartialVideogame() throws Exception {
        when(games.save(any(VideogameEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        mvc.perform(post("/videogame").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"videogameName\":\"Game\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.videogameName").value("Game"));
        verify(games).save(any(VideogameEntity.class));
    }
    @Test
    void putRejectsCoercionsOutOfRangeFlagsAndNullRequiredValues() throws Exception {
        String complete = "{\"videogameName\":\"Game\",\"videogameType\":\"RPG\",\"videogameYear\":\"2024\",\"platformId\":4,\"digitalPlatformId\":5,\"companyName\":\"Studio\",\"isDigital\":1,\"isCompleted\":0,\"isPlatinum\":1,\"multiplayer\":true}";
        for (String invalid : new String[]{
                complete.replace("\"platformId\":4", "\"platformId\":\"4\""),
                complete.replace("\"platformId\":4", "\"platformId\":4.5"),
                complete.replace("\"isDigital\":1", "\"isDigital\":2"),
                complete.replace("\"isCompleted\":0", "\"isCompleted\":-1"),
                complete.replace("\"isPlatinum\":1", "\"isPlatinum\":true"),
                complete.replace("\"multiplayer\":true", "\"multiplayer\":1"),
                complete.replace("\"videogameName\":\"Game\"", "\"videogameName\":null"),
                complete.replace("\"videogameYear\":\"2024\"", "\"videogameYear\":2024")}) {
            mvc.perform(put("/videogame/3").contentType(MediaType.APPLICATION_JSON).content(invalid))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(games, platforms, stores);
    }

}
