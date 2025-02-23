package com.nhl4j.serializers.espn.statparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;
import com.nhl4j.serializers.StatHelper;

import java.util.List;
import java.util.Map;

import static com.nhl4j.domain.Stat.*;
import static com.nhl4j.domain.Stat.SACKS;
import static com.nhl4j.serializers.StatHelper.getMaxStat;
import static com.nhl4j.serializers.StatHelper.getSumOfPlayerStat;
import static com.nhl4j.util.JsonUtil.getFirstNodeFromArrayByKey;
import static com.nhl4j.util.JsonUtil.streamOf;

public class FootballStatParser extends StatParser {

    private static final Map<String, List<Stat>> NFL_PLAYER_STAT_CATEGORIES = Map.of(
            "passing", List.of(PASSING_YARDS, PASSING_COMPLETIONS, PASSING_TDS, PASSING_INTS),
            "rushing", List.of(RUSHING_YARDS, RUSHING_ATTEMPTS, RUSHING_TDS, RUSHING_LONG),
            "receiving", List.of(RECEIVING_YARDS, RECEPTIONS, RECEIVING_TDS, RECEIVING_LONG),
            "kicking", List.of(FIELD_GOALS, FIELD_GOALS_LONG, KICKING_POINTS),
            "punting", List.of(PUNTS, PUNTS_LONG, PUNT_YARDS)
    );

    @Override
    public void parseGameStats(Game game) {
        StatHelper.buildGameStats(game);
        game.getStats().put(RECEIVING_LONG, getMaxStat(game, RECEIVING_LONG));
        game.getStats().put(RUSHING_LONG, getMaxStat(game, RUSHING_LONG));
        game.getStats().put(FIELD_GOALS_LONG, getMaxStat(game, FIELD_GOALS_LONG));
        game.getStats().put(PUNTS_LONG, getMaxStat(game, PUNTS_LONG));
    }

    @Override
    public void parseTeamStats(Team team, JsonNode teamNode) {
        final var teamStats = team.getStats();

        final var statisticsNode = (ArrayNode) teamNode.get("statistics");

        teamStats.put(INTERCEPTIONS, getTeamStat(INTERCEPTIONS, statisticsNode));
        teamStats.put(FUMBLES, getTeamStat(FUMBLES, statisticsNode));
        teamStats.put(TURNOVERS, getTeamStat(TURNOVERS, statisticsNode));

        teamStats.put(FIELD_GOALS, getSumOfPlayerStat(team, FIELD_GOALS));
        teamStats.put(KICKING_POINTS, getSumOfPlayerStat(team, KICKING_POINTS));
        teamStats.put(PUNTS, getSumOfPlayerStat(team, PUNTS));
        teamStats.put(PUNT_YARDS, getSumOfPlayerStat(team, PUNT_YARDS));
        teamStats.put(RUSHING_TDS, getSumOfPlayerStat(team, RUSHING_TDS));
        teamStats.put(PASSING_TDS, getSumOfPlayerStat(team, PASSING_TDS));

        // Need to parse out the sack yards value
        final var sacks = getTeamStat(SACKS, statisticsNode).split("-")[0];
        teamStats.put(SACKS, sacks);
    }

    @Override
    public void parsePlayerStats(Team team, JsonNode playerStatsNode) {
        for (final var statCategory : NFL_PLAYER_STAT_CATEGORIES.entrySet()) {
            final var categoryNode = getFirstNodeFromArrayByKey(
                    playerStatsNode.get("statistics"), "name", statCategory.getKey());

            parsePlayerStatCategory(team, categoryNode, statCategory.getValue());
        }
    }

    @Override
    public void parseGameEvents(Game game, JsonNode gameNode) {
        final var events = streamOf(gameNode.get("scoringPlays"))
                .map(this::parseEventFromScoringPlaysNode)
                .toList();

        game.setEvents(events);
    }

    private Game.Event parseEventFromScoringPlaysNode(JsonNode eventNode) {
        return new Game.Event(
                eventNode.get("id").textValue(),
                eventNode.get("period").get("number").intValue(),
                eventNode.get("clock").get("displayValue").textValue(),
                getScoringType(eventNode.get("scoringType")),
                eventNode.get("team").get("id").textValue(),
                List.of());
    }

    private Stat getScoringType(JsonNode scoringTypeNode) {
        if (scoringTypeNode.get("name").textValue().equals("field-goal")) {
            return FIELD_GOALS;
        } else if (scoringTypeNode.get("name").textValue().equals("touchdown")) {
            return TDS;
        }
        return OTHER;
    }


}
