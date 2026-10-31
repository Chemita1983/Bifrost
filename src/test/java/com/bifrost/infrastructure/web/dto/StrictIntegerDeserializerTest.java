package com.bifrost.infrastructure.web.dto;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.exc.InputCoercionException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class StrictIntegerDeserializerTest {
    @Test
    void acceptsIntegerRejectsOtherTokensAndOverflow() throws Exception {
        var deserializer = new StrictIntegerDeserializer();
        var context = mock(DeserializationContext.class);
        try (JsonParser parser = new JsonFactory().createParser("0")) {
            parser.nextToken();
            assertThat(deserializer.deserialize(parser, context)).isZero();
        }
        try (JsonParser parser = new JsonFactory().createParser("1.5")) {
            parser.nextToken();
            when(context.handleUnexpectedToken(Integer.class, parser)).thenThrow(new IllegalArgumentException());
            assertThatThrownBy(() -> deserializer.deserialize(parser, context)).isInstanceOf(IllegalArgumentException.class);
        }
        try (JsonParser parser = new JsonFactory().createParser("2147483648")) {
            parser.nextToken();
            assertThatThrownBy(() -> deserializer.deserialize(parser, context)).isInstanceOf(InputCoercionException.class);
        }
    }
}
