package com.nhl4j.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.TeamInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamInfoDeserializer extends StdDeserializer<TeamInfo> {

    public TeamInfoDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public TeamInfoDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
    }

    @Override
    public TeamInfo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode teamNode = jsonParser.getCodec().readTree(jsonParser);

        final var teamInfo = new TeamInfo();
        teamInfo.setId(teamNode.get("id").intValue());
        teamInfo.setName(teamNode.get("name").textValue());
        teamInfo.setTeamName(teamNode.get("teamName").textValue());
        teamInfo.setAbbreviation(teamNode.get("abbreviation").textValue());

        if (teamNode.get("roster") != null) {
            final var rosterNode = teamNode.get("roster").get("roster");
            teamInfo.setRoster(buildPlayers(rosterNode));
        }

        return teamInfo;
    }

    private List<TeamInfo.Player> buildPlayers(JsonNode playersNode) {
        final var playerList = new ArrayList<TeamInfo.Player>();

        final var playerElements = playersNode.elements();

        while (playerElements.hasNext()) {
            final var playerNode = playerElements.next();
            final var person = playerNode.get("person");
            playerList.add(TeamInfo.Player.builder()
                    .id(person.get("id").intValue())
                    .fullName(person.get("fullName").textValue())
                    .position(playerNode.get("position").get("abbreviation").textValue())
                    .number(playerNode.get("jerseyNumber").textValue())
                    .build());
        }

        return playerList;
    }
}
