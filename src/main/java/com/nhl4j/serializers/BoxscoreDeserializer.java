package com.nhl4j.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.Player;
import com.nhl4j.domain.game.Team;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BoxscoreDeserializer extends StdDeserializer<Boxscore> {

    private final ObjectMapper objectMapper;

    public BoxscoreDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public BoxscoreDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Boxscore deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode teamsNode = gameNode.get("teams");

        JsonNode awayTeamNode = teamsNode.get("away");
        val awayTeam = buildTeamData(awayTeamNode);
        JsonNode homeTeamNode = teamsNode.get("home");
        val homeTeam = buildTeamData(homeTeamNode);

        val boxscore = new Boxscore();
        boxscore.setAwayTeam(awayTeam);
        boxscore.setHomeTeam(homeTeam);

        return boxscore;
    }

    private Team buildTeamData(JsonNode teamNode) {
        val team = new Team();

        val teamDataNode = teamNode.get("team");
        team.setId(teamDataNode.get("id").textValue());
        team.setName(teamDataNode.get("name").textValue());

        val statsNode = teamNode.get("teamStats").get("teamSkaterStats");
        team.setScore(statsNode.get("goals").intValue());
        team.setPenaltyMinutes(statsNode.get("pim").intValue());
        team.setShotsOnGoal(statsNode.get("shots").intValue());
        team.setHits(statsNode.get("hits").intValue());

        team.setPlayers(buildPlayers(teamNode.get("players")));

        return team;
    }

    private List<Player> buildPlayers(JsonNode playersNode) {
        val playerList = new ArrayList<Player>();

        val playerElements = playersNode.elements();

        while(playerElements.hasNext()) {
            val playerNode = playerElements.next();
            playerList.add(buildPlayer(playerNode));
        }

        return playerList;
    }

    private Player buildPlayer(JsonNode playerNode) {
        val player = new Player();

        val playerInfo = playerNode.get("person");
        player.setId(playerInfo.get("id").intValue());
        player.setFullName(playerInfo.get("fullName").textValue());

        val stats = playerNode.get("stats").get("skaterStats");
        if (stats != null) {
            player.setGoals(stats.get("goals").intValue());
            player.setAssists(stats.get("assists").intValue());
            player.setPenaltyMins(stats.get("penaltyMinutes").intValue());
            player.setShotsOnGoal(stats.get("shots").intValue());
        }

        return player;
    }
}
