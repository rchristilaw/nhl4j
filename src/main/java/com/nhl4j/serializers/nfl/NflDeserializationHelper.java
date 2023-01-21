package com.nhl4j.serializers.nfl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhl4j.domain.game.Team;

public class NflDeserializationHelper {

    public static Team parseTeamFromCompetitionsNode(JsonNode competitionNode, String homeAway) {
        final var team = new Team();
        final var competitorNodes = competitionNode.get("competitors");

        for (final var competitorNode : competitorNodes) {
            if (!competitorNode.get("homeAway").textValue().equals(homeAway)) {
                continue;
            }
            final var teamNode = competitorNode.get("team");
            team.setId(teamNode.get("id").toString());
            team.setName(teamNode.get("displayName").textValue());
            team.setShortName(teamNode.get("name").textValue());
            team.setAbbreviation(teamNode.get("abbreviation").textValue());
            return team;
        }
        return null;
    }
}
