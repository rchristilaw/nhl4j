package com.nhl4j.serializers.espn.statparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.Game;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;
import com.nhl4j.serializers.StatHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.nhl4j.domain.Stat.*;
import static com.nhl4j.util.JsonUtil.*;

public class MlbStatParser extends StatParser {

    private static final Map<String, List<Stat>> MLB_STAT_CATEGORIES = Map.of(
            "batting", List.of(MLB_K, MLB_RBI, MLB_HITS, MLB_RUNS, MLB_DOUBLES, MLB_TRIPLES,
                    MLB_HR, MLB_GRAND_SLAMS, MLB_XBH, MLB_TB, MLB_SB, MLB_AB,
                    MLB_BB, MLB_HBP, MLB_GIDP, MLB_RUNNER_LOB, MLB_CAUGHT_STEALING),
            "pitching", List.of(MLB_PITCHER_K, MLB_PITCHER_BB, MLB_PITCHER_RBI, MLB_PITCHER_HITS,
                    MLB_PITCHER_RUNS, MLB_PITCHER_DOUBLES, MLB_PITCHER_TRIPLES, MLB_PITCHER_HR,
                    MLB_PITCHER_WILD_PITCHES, MLB_PITCHER_BALKS, MLB_PITCHER_INTENTIONAL_BB, MLB_PITCHER_HBP,
                    MLB_PITCHER_IP, MLB_PITCHER_BATTERS_FACED, MLB_PITCHER_OPPONENTS_TB, MLB_PITCHER_PITCHES, MLB_PITCHER_SAVES,
                    MLB_PITCHER_BLOWN_SAVES, MLB_PITCHER_SHUTOUTS, MLB_PITCHER_W, MLB_PITCHER_L),
            "fielding", List.of(MLB_ERRORS, MLB_OUTFIELD_ASSISTS, MLB_DOUBLE_PLAYS, MLB_TRIPLE_PLAYS)
    );

    @Override
    public void parseGameStats(Game game) {
        StatHelper.buildGameStats(game);
    }

    @Override
    public void parseTeamStats(Team team, JsonNode teamNode) {
        final var teamStats = team.getStats();

        final var statisticsNode = (ArrayNode) teamNode.get("statistics");

        for (final var statCategory : MLB_STAT_CATEGORIES.keySet()) {
            final var categoryNode = getFirstNodeFromArrayByKey(statisticsNode, "name", statCategory);

            for (final var stat : MLB_STAT_CATEGORIES.get(statCategory)) {
                final var categoryStatsNode = categoryNode.get("stats");

                final var statNode = getFirstNodeFromArrayByKey(categoryStatsNode, "name", stat.getKey());
                if (statNode != null) {
                    teamStats.put(stat, statNode.get("displayValue").textValue());
                }
            }
        }
    }

    @Override
    public void parsePlayerStats(Team team, JsonNode playerStatsNode) {
        for (final var statCategory : MLB_STAT_CATEGORIES.entrySet()) {
            final var categoryNode = getFirstNodeFromArrayByKey(
                    playerStatsNode.get("statistics"), "type", statCategory.getKey());

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
                eventNode.get("period").get("type").textValue(),
                SCORE,
                eventNode.get("team").get("id").textValue(),
                List.of(eventNode.get("participants").elements().next().get("athlete").get("id").textValue()));
    }
}
