package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nhl4j.domain.Stat.*;
import static com.nhl4j.serializers.StatHelper.getMaxStat;
import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseGameStatusFromCompetitionNode;
import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseTeamFromCompetitionNode;

public class EspnGameDeserializer extends StdDeserializer<Game> {


    public EspnGameDeserializer(ApiSource source) {
        this(null, source);
    }

    public EspnGameDeserializer(Class<?> vc, ApiSource apiSource) {
        super(vc);
        this.apiSource = apiSource;
    }

    private ApiSource apiSource;

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        final var competitionNode = gameNode.get("header").get("competitions").get(0);

        final var game = new Game();
        game.setId(competitionNode.get("id").textValue());
        final var dateString = competitionNode.get("date").textValue();
        game.setGameDate(dateString);

        final var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            game.setDate(sdf.parse(dateString).toInstant());
        } catch (ParseException ignored) {
        }

        game.setGameStatus(parseGameStatusFromCompetitionNode(competitionNode));

        game.setHome(parseTeamFromCompetitionNode(competitionNode, "home", true));
        game.setAway(parseTeamFromCompetitionNode(competitionNode, "away", true));
        game.getHome().setRoster(new ArrayList<>());
        game.getAway().setRoster(new ArrayList<>());

        final var boxscoreNode = gameNode.get("boxscore");
        parseTeamStats(boxscoreNode, game.getHome());
        parseTeamStats(boxscoreNode, game.getAway());

        apiSource.getStatParser().parseGameEvents(game, gameNode);
        apiSource.getStatParser().parseBettingLine(game, gameNode);
        apiSource.getStatParser().parseGameStats(game);

        return game;
    }

    private void parseTeamStats(JsonNode boxscoreNode, Team team) {
        if (boxscoreNode.get("players") != null) {
            parsePlayerStats(team, (ArrayNode) boxscoreNode.get("players"));
        }

        if (boxscoreNode.get("teams") != null) {
            for (final var teamsNode : boxscoreNode.get("teams")) {
                if (!teamsNode.get("team").get("id").textValue().equals(team.getId())) {
                    continue;
                }

                apiSource.getStatParser().parseTeamStats(team, teamsNode);
            }
        }
    }

    private void parsePlayerStats(Team team, ArrayNode teamPlayerStatsNode) {
        for (final var playerStatsNode : teamPlayerStatsNode) {
            if (!playerStatsNode.get("team").get("id").textValue().equals(team.getId())) {
                continue;
            }

            apiSource.getStatParser().parsePlayerStats(team, playerStatsNode);
        }
    }
}
