package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.GameStatus;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;

public class NflDeserializationHelper {

    public static Team parseTeamFromCompetitionNode(JsonNode competitionNode, String homeAway, boolean isBoxscore) {
        final var team = new Team();
        final var competitorNodes = competitionNode.get("competitors");

        for (final var competitorNode : competitorNodes) {
            if (!competitorNode.get("homeAway").textValue().equals(homeAway)) {
                continue;
            }
            final var teamNode = competitorNode.get("team");
            team.setId(teamNode.get("id").textValue());
            team.setFullName(teamNode.get("displayName").textValue());
            team.setNickName(teamNode.get("name").textValue());
            team.setCityName(teamNode.get("name").textValue());
            team.setAbbreviation(teamNode.get("abbreviation").textValue());

            if (competitorNode.get("score") != null) {
                team.getStats().put(Stat.SCORE, competitorNode.get("score").textValue());
            }

            if (isBoxscore && competitorNode.get("linescores") != null) {
                final var linescoresNode = (ArrayNode)competitorNode.get("linescores");
                team.getStats().put(Stat.SCORE_Q1, getLineScore(linescoresNode, 0));
                team.getStats().put(Stat.SCORE_Q2, getLineScore(linescoresNode, 1));
                team.getStats().put(Stat.SCORE_Q3, getLineScore(linescoresNode, 2));
                team.getStats().put(Stat.SCORE_Q4, getLineScore(linescoresNode, 3));
            }

            return team;
        }
        return null;
    }

    private static String getLineScore(ArrayNode linescoresNode, int index) {
        return linescoresNode.get(index) != null
                ? linescoresNode.get(index).get("displayValue").textValue() : "0";
    }

    public static GameStatus parseGameStatusFromCompetitionNode(JsonNode competitionNode) {
        final var statusValue = competitionNode.get("status").get("type").get("name").textValue();
        if (statusValue.equals("STATUS_FINAL")) {
            return GameStatus.FINAL;
        } else if (statusValue.equals("STATUS_SCHEDULED")) {
            return GameStatus.UPCOMING;
        } else {
            return GameStatus.LIVE;
        }
    }
}
