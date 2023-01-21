package com.nhl4j.serializers.nhl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.game.Game;
import com.nhl4j.domain.game.GameStatus;
import com.nhl4j.domain.game.Team;
import lombok.val;

import java.io.IOException;

public class NhlGameDeserializer extends StdDeserializer<Game> {

    private final ObjectMapper objectMapper;

    public NhlGameDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public NhlGameDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode teamsNode = gameNode.get("teams");
        Team awayTeam = objectMapper.treeToValue(teamsNode.get("away").get("team"), Team.class);
        Team homeTeam = objectMapper.treeToValue(teamsNode.get("home").get("team"), Team.class);

        val game = new Game();
        game.setId(gameNode.get("gamePk").toString());
        game.setHome(homeTeam);
        game.setAway(awayTeam);
        game.setGameDate(gameNode.get("gameDate").textValue());
        game.setGameStatus(parseGameStatus(gameNode.get("status")));

        return game;
    }

    private GameStatus parseGameStatus(JsonNode statusNode) {
        //TODO parse NHL game status
        return GameStatus.LIVE;
    }
}
