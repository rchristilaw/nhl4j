package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.game.GameStatus;

import java.io.IOException;

public class NflBoxscoreDeserializer extends StdDeserializer<Boxscore> {

    private final ObjectMapper objectMapper;

    public NflBoxscoreDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public NflBoxscoreDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Boxscore deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        final var competitionNode = gameNode.get("header").get("competitions").get(0);

        final var boxscore = new Boxscore();
        boxscore.setId(competitionNode.get("id").toString());
        boxscore.setGameDate(competitionNode.get("date").toString());
        boxscore.setGameStatus(parseGameStatus(competitionNode.get("status").get("type")));

        boxscore.setHomeTeam(NflDeserializationHelper.parseTeamFromCompetitionsNode(competitionNode, "home"));
        boxscore.setAwayTeam(NflDeserializationHelper.parseTeamFromCompetitionsNode(competitionNode, "away"));

        return boxscore;
    }

    private GameStatus parseGameStatus(JsonNode statusNode) {
        final var statusValue = statusNode.get("name").toString();
        if (statusValue.equals("STATUS_FINAL")) {
            return GameStatus.FINAL;
        } else if (statusValue.equals("STATUS_SCHEDULED")) {
            return GameStatus.UPCOMING;
        } else {
            return GameStatus.LIVE;
        }
    }

}
