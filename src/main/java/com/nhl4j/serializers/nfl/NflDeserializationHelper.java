package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhl4j.domain.GameStatus;
import com.nhl4j.domain.Player;
import com.nhl4j.domain.Team;

public class NflDeserializationHelper {

    public static Team parseTeamFromCompetitionNode(JsonNode competitionNode, String homeAway) {
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
            return team;
        }
        return null;
    }

    public static GameStatus parseGameStatusFromCompetitionNode(JsonNode competitionNode) {
        final var statusValue = competitionNode.get("status").get("type").get("name").toString();
        if (statusValue.equals("STATUS_FINAL")) {
            return GameStatus.FINAL;
        } else if (statusValue.equals("STATUS_SCHEDULED")) {
            return GameStatus.UPCOMING;
        } else {
            return GameStatus.LIVE;
        }
    }
}
