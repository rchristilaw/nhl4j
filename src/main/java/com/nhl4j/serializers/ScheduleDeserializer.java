package com.nhl4j.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.schedule.Schedule;
import com.nhl4j.domain.game.Game;
import lombok.val;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ScheduleDeserializer extends StdDeserializer<Schedule> {

    private ObjectMapper objectMapper;

    public ScheduleDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public ScheduleDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode scheduleNode = jsonParser.getCodec().readTree(jsonParser);

        val schedule = new Schedule();
        schedule.setTotalGames(scheduleNode.get("totalGames").intValue());

        val gamesNode = scheduleNode.get("dates").get(0).get("games");
        List<Game> games = Arrays.asList(objectMapper.treeToValue(gamesNode, Game[].class));
        schedule.setGames(games);

        return schedule;
    }
}
