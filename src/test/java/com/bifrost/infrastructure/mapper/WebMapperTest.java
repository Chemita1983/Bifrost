package com.bifrost.infrastructure.mapper;

import com.bifrost.infrastructure.web.dto.VideogameRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebMapperTest {
    private final WebMapper mapper = new WebMapper();

    @Test
    void mapsHoursBetweenRequestDomainAndResponse() {
        for (Integer hours : new Integer[] {null, 0, 42}) {
            var request = new VideogameRequest("Game", null, null, null, null, null, 0, 0, 0, false, hours);
            var domain = mapper.toDomain(request);
            assertThat(domain.getHoursPlayed()).isEqualTo(hours);
            assertThat(mapper.toRequest(domain).hoursPlayed()).isEqualTo(hours);
            assertThat(mapper.toResponse(domain).hoursPlayed()).isEqualTo(hours);
        }
    }
}
