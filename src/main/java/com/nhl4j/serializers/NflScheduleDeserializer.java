package com.nhl4j.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.game.Game;
import com.nhl4j.domain.game.GameStatus;
import com.nhl4j.domain.game.Team;
import com.nhl4j.domain.schedule.Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NflScheduleDeserializer extends StdDeserializer<Schedule> {

    private final ObjectMapper objectMapper;

    public NflScheduleDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public NflScheduleDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode scheduleNode = jsonParser.getCodec().readTree(jsonParser);

        final var schedule = new Schedule();

        final var events = scheduleNode.get("events");

        if (events != null) {
            schedule.setGames(parseEventsToGames(events));
        }

        schedule.setTotalGames(schedule.getGames().size());
        return schedule;
    }

    private List<Game> parseEventsToGames(JsonNode events) {
        final var games = new ArrayList<Game>();

        for (final var event : events) {
            final var game = new Game();
            game.setId(event.get("id").textValue());
            game.setGameDate(event.get("date").textValue());
            game.setHome(parseTeam(event, "home"));
            game.setAway(parseTeam(event, "away"));

            game.setGameStatus(new GameStatus());
            games.add(game);
        }

        return games;
    }

    private Team parseTeam(JsonNode event, String homeAway) {
        final var team = new Team();
        final var competitorNodes = event.get("competitions").get(0).get("competitors");

        for (final var competitorNode : competitorNodes) {
            if (!competitorNode.get("homeAway").textValue().equals(homeAway)) {
                continue;
            }
            final var teamNode = competitorNode.get("team");
            team.setId(teamNode.get("id").textValue());
            team.setName(teamNode.get("displayName").textValue());
            team.setShortName(teamNode.get("name").textValue());
            team.setAbbreviation(teamNode.get("abbreviation").textValue());
            return team;
        }
        return null;
    }

}
