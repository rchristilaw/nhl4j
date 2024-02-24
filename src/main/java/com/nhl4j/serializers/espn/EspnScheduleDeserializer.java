package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseGameStatusFromCompetitionNode;
import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseTeamFromCompetitionNode;

public class EspnScheduleDeserializer extends StdDeserializer<Schedule> {

    public EspnScheduleDeserializer() {
        this(null);
    }

    public EspnScheduleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Schedule deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode scheduleNode = jsonParser.getCodec().readTree(jsonParser);

        final var schedule = new Schedule();

        final var events = scheduleNode.get("events");

        if (events != null) {
            schedule.getGames().addAll(parseEventsToGames(events));
        }

        return schedule;
    }

    private List<Game> parseEventsToGames(JsonNode events) {
        final var games = new ArrayList<Game>();

        for (final var event : events) {
            final var game = new Game();
            game.setId(event.get("id").textValue());
            game.setGameDate(event.get("date").textValue());

            final var competitionNode = event.get("competitions").get(0);
            game.setHome(parseTeamFromCompetitionNode(competitionNode, "home", false));
            game.setAway(parseTeamFromCompetitionNode(competitionNode, "away", false));

            game.setGameStatus(parseGameStatusFromCompetitionNode(competitionNode));
            games.add(game);
        }

        return games;
    }



}
