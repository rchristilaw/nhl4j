package com.nhl4j.serializers.espn.statparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;
import com.nhl4j.serializers.StatHelper;

import java.util.*;

import static com.nhl4j.domain.Stat.*;
import static com.nhl4j.serializers.StatHelper.getSumOfPlayerStat;
import static com.nhl4j.util.JsonUtil.getFirstNodeFromArrayByKey;
import static com.nhl4j.util.JsonUtil.streamOf;

public class HockeyStatParser extends StatParser {

    private static final Map<String, List<Stat>> NHL_PLAYER_STAT_CATEGORIES = Map.of(
            "forwards", List.of(GOALS, ASSISTS, GOALS_PP, GOALS_SH, PIMS, SOGS, HITS, FACEOFF_WINS),
            "defenses", List.of(GOALS, ASSISTS, GOALS_PP, GOALS_SH, PIMS, SOGS, HITS, FACEOFF_WINS),
            "goalies", List.of(SAVES)
    );

    @Override
    public void parseGameStats(Game game) {
        StatHelper.buildGameStats(game);
    }

    @Override
    public void parseTeamStats(Team team, JsonNode teamNode) {
        final var teamStats = team.getStats();

        final var statisticsNode = (ArrayNode) teamNode.get("statistics");
        teamStats.put(GOALS_PP, getTeamStat(GOALS_PP, statisticsNode));
        teamStats.put(GOALS_SH, getTeamStat(GOALS_SH, statisticsNode));
        teamStats.put(PIMS, getTeamStat(PIMS, statisticsNode));
        teamStats.put(SOGS, getTeamStat(SOGS, statisticsNode));
        teamStats.put(HITS, getTeamStat(HITS, statisticsNode));
        teamStats.put(FACEOFF_WINS, getTeamStat(FACEOFF_WINS, statisticsNode));
    }

    @Override
    public void parsePlayerStats(Team team, JsonNode playerStatsNode) {
        for (final var statCategory : NHL_PLAYER_STAT_CATEGORIES.entrySet()) {
            final var categoryNode = getFirstNodeFromArrayByKey(
                    playerStatsNode.get("statistics"), "name", statCategory.getKey());

            parsePlayerStatCategory(team, categoryNode, statCategory.getValue());
        }
    }

    @Override
    public void parseGameEvents(Game game, JsonNode gameNode) {
        final var events = streamOf(gameNode.get("plays"))
                .map(this::parseEventFromPlaysNode)
                .filter(Objects::nonNull)
                .toList();

        game.setEvents(events);
    }

    private Game.Event parseEventFromPlaysNode(JsonNode eventNode) {
        if (!eventNode.get("scoringPlay").booleanValue()) {
            return null;
        }

        return new Game.Event(
                eventNode.get("sequenceNumber").textValue(),
                eventNode.get("period").get("number").intValue(),
                eventNode.get("clock").get("displayValue").textValue(),
                SCORE,
                eventNode.get("team").get("id").textValue(),
                List.of(eventNode.get("participants").elements().next().get("athlete").get("id").textValue()));
    }
}
