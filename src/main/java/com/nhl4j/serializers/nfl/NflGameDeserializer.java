package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Game;

import java.io.IOException;

import static com.nhl4j.serializers.nfl.NflDeserializationHelper.parseGameStatusFromCompetitionNode;
import static com.nhl4j.serializers.nfl.NflDeserializationHelper.parseTeamFromCompetitionNode;

public class NflGameDeserializer extends StdDeserializer<Game> {

    public NflGameDeserializer() {
        this(null);
    }

    public NflGameDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        final var competitionNode = gameNode.get("header").get("competitions").get(0);

        final var game = new Game();
        game.setId(competitionNode.get("id").toString());
        game.setGameDate(competitionNode.get("date").toString());
        game.setGameStatus(parseGameStatusFromCompetitionNode(competitionNode));

        game.setHome(parseTeamFromCompetitionNode(competitionNode, "home"));
        game.setAway(parseTeamFromCompetitionNode(competitionNode, "away"));

        return game;
    }

}
