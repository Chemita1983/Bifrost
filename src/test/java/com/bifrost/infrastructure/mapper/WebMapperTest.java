package com.bifrost.infrastructure.mapper;

import com.bifrost.domain.Videogame;
import com.bifrost.infrastructure.web.dto.VideogameRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebMapperTest {
    private final WebMapper mapper = new WebMapper();

    @Test
    void mapsHoursBetweenRequestDomainAndResponse() {
        for (Integer hours : new Integer[] {null, 0, 42}) {
            var request = new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, hours, null);
            var domain = mapper.toDomain(request);
            assertThat(domain.getHoursPlayed()).isEqualTo(hours);
            assertThat(mapper.toRequest(domain).hoursPlayed()).isEqualTo(hours);
            assertThat(mapper.toResponse(domain).hoursPlayed()).isEqualTo(hours);
        }
    }

    @Test
    void mapsCoverUrlBetweenRequestDomainAndResponse() {
        for (String url : new String[] {null, "https://example.com/cover.jpg"}) {
            var request = new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, null, url);
            var domain = mapper.toDomain(request);
            assertThat(domain.getCoverUrl()).isEqualTo(url);
            assertThat(mapper.toRequest(domain).coverUrl()).isEqualTo(url);
            assertThat(mapper.toResponse(domain).coverUrl()).isEqualTo(url);
        }
    }

    @Test
    void steamAppIdIsReturnedButCannotComeFromARequest() {
        var request = new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, null, null);
        assertThat(mapper.toDomain(request).getSteamAppId()).isNull();

        Videogame steamGame = new Videogame();
        steamGame.setSteamAppId(620);
        assertThat(mapper.toResponse(steamGame).steamAppId()).isEqualTo(620);
    }
}
