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
import lombok.val;

import java.io.IOException;

public class GameDeserializer extends StdDeserializer<Game> {

    private ObjectMapper objectMapper;

    public GameDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public GameDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        GameStatus status = objectMapper.treeToValue(gameNode.get("status"), GameStatus.class);

        JsonNode teamsNode = gameNode.get("teams");
        Team awayTeam = objectMapper.treeToValue(teamsNode.get("away"), Team.class);
        Team homeTeam = objectMapper.treeToValue(teamsNode.get("home"), Team.class);

        val game = new Game();
        game.setId(gameNode.get("gamePk").intValue());
        game.setHome(homeTeam);
        game.setAway(awayTeam);
        game.setGameDate(gameNode.get("gameDate").textValue());
        game.setGameStatus(status);

        return game;
    }
}
