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
import static com.nhl4j.util.JsonUtil.*;

public class BaseballStatParser extends StatParser {

    private static final Map<String, List<Stat>> MLB_PLAYER_STAT_CATEGORIES = Map.of(
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
    }

    @Override
    public void parsePlayerStats(Team team, JsonNode playerStatsNode) {
        for (final var statCategory : MLB_PLAYER_STAT_CATEGORIES.entrySet()) {
            final var categoryNode = getFirstNodeFromArrayByKey(
                    playerStatsNode.get("statistics"), "type", statCategory.getKey());

            parsePlayerStatCategory(team, categoryNode, statCategory.getValue());
        }
    }

    @Override
    public void parseGameEvents(Game game, JsonNode gameNode) {

    }
}
