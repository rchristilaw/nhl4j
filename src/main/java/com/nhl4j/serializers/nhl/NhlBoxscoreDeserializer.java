package com.nhl4j.serializers.nhl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.game.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NhlBoxscoreDeserializer extends StdDeserializer<Boxscore> {

    private final ObjectMapper objectMapper;

    public NhlBoxscoreDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public NhlBoxscoreDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Boxscore deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        JsonNode teamsNode = gameNode.get("teams");

        JsonNode awayTeamNode = teamsNode.get("away");
        final var awayTeam = buildTeamData(awayTeamNode);
        JsonNode homeTeamNode = teamsNode.get("home");
        final var homeTeam = buildTeamData(homeTeamNode);

        final var boxscore = new Boxscore();
        boxscore.setAwayTeam(awayTeam);
        boxscore.setHomeTeam(homeTeam);

        return boxscore;
    }

    private Team buildTeamData(JsonNode teamNode) {
        final var team = new Team();

        final var teamDataNode = teamNode.get("team");
        team.setId(teamDataNode.get("id").toString());
        team.setName(teamDataNode.get("name").textValue());

        final var statsNode = teamNode.get("teamStats").get("teamSkaterStats");
        team.setScore(statsNode.get("goals").intValue());
        team.setPenaltyMinutes(statsNode.get("pim").intValue());
        team.setShotsOnGoal(statsNode.get("shots").intValue());
        team.setHits(statsNode.get("hits").intValue());

        team.setPlayers(buildPlayers(teamNode.get("players")));

        return team;
    }

    private List<Player> buildPlayers(JsonNode playersNode) {
        final var playerList = new ArrayList<Player>();

        final var playerElements = playersNode.elements();

        while(playerElements.hasNext()) {
            final var playerNode = playerElements.next();
            playerList.add(buildPlayer(playerNode));
        }

        return playerList;
    }

    private Player buildPlayer(JsonNode playerNode) {
        final var player = new Player();

        final var playerInfo = playerNode.get("person");
        player.setId(playerInfo.get("id").toString());
        player.setFullName(playerInfo.get("fullName").textValue());

        final var stats = playerNode.get("stats").get("skaterStats");
        if (stats != null) {
            player.getStats().put(Stat.GOALS, stats.get("goals").textValue());
            player.getStats().put(Stat.ASSISTS, stats.get("assists").textValue());
            player.getStats().put(Stat.PIMS, stats.get("penaltyMinutes").textValue());
            player.getStats().put(Stat.SOGS, stats.get("shots").textValue());
        }

        return player;
    }
}
