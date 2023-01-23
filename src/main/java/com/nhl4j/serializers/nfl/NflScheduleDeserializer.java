package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nhl4j.serializers.nfl.NflDeserializationHelper.parseGameStatusFromCompetitionNode;
import static com.nhl4j.serializers.nfl.NflDeserializationHelper.parseTeamFromCompetitionNode;

public class NflScheduleDeserializer extends StdDeserializer<Schedule> {

    public NflScheduleDeserializer() {
        this(null);
    }

    public NflScheduleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

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

            final var competitionNode = event.get("competitions").get(0);
            game.setHome(parseTeamFromCompetitionNode(competitionNode, "home"));
            game.setAway(parseTeamFromCompetitionNode(competitionNode, "away"));

            game.setGameStatus(parseGameStatusFromCompetitionNode(competitionNode));
            games.add(game);
        }

        return games;
    }



}
