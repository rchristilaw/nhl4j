package com.nhl4j.serializers.nhl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.GameStatus;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;
import com.nhl4j.serializers.StatHelper;

import java.io.IOException;
import java.util.HashMap;

import static com.nhl4j.serializers.nhl.NhlDeserializationHelper.parseRosterFromPlayersNode;

public class NhlGameDeserializer extends StdDeserializer<Game> {

    public NhlGameDeserializer() {
        this(null);
    }

    public NhlGameDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        final var game = new Game();
        game.setId(gameNode.get("gamePk").toString());

        final JsonNode teamsNode;
        final var fullGameDataNode = gameNode.get("gameData");
        if (fullGameDataNode != null) {
            game.setGameDate(fullGameDataNode.get("datetime").get("dateTime").textValue());
            game.setGameStatus(parseGameStatus(fullGameDataNode.get("status")));

            teamsNode = gameNode.get("liveData").get("boxscore").get("teams");
        } else {
            game.setGameDate(gameNode.get("gameDate").textValue());
            game.setGameStatus(parseGameStatus(gameNode.get("status")));

            teamsNode = gameNode.get("teams");
        }

        JsonNode awayTeamNode = teamsNode.get("away");
        final var awayTeam = buildTeamData(awayTeamNode);
        JsonNode homeTeamNode = teamsNode.get("home");
        final var homeTeam = buildTeamData(homeTeamNode);
        game.setHome(homeTeam);
        game.setAway(awayTeam);

        StatHelper.buildGameStats(game);

        return game;
    }

    private Team buildTeamData(JsonNode teamNode) {
        final var team = new Team();

        final var teamDataNode = teamNode.get("team");
        team.setId(teamDataNode.get("id").toString());
        team.setFullName(teamDataNode.get("name").textValue());

        if (teamNode.get("teamStats") != null) {
            final var statsNode = teamNode.get("teamStats").get("teamSkaterStats");
            final var stats = new HashMap<Stat, String>();
            stats.put(Stat.GOALS, statsNode.get("goals").toString());
            stats.put(Stat.GOALS_PP, statsNode.get("powerPlayGoals").toString());
            stats.put(Stat.PIMS, statsNode.get("pim").toString());
            stats.put(Stat.SOGS, statsNode.get("shots").toString());
            stats.put(Stat.HITS, statsNode.get("hits").toString());

            team.setStats(stats);
        }

        if (teamNode.get("players") != null) {
            team.setRoster(parseRosterFromPlayersNode(teamNode.get("players")));
        }

        return team;
    }

    private GameStatus parseGameStatus(JsonNode statusNode) {
        if (statusNode.get("codedGameState").textValue().equals("7")) {
            return GameStatus.FINAL;
        } else if (statusNode.get("codedGameState").textValue().equals("1")) {
            return GameStatus.UPCOMING;
        }
        return GameStatus.LIVE;
    }
}
