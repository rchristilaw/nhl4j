package com.nhl4j.serializers.nhl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nhl4j.serializers.nhl.NhlDeserializationHelper.parseRosterFromPlayersNode;

public class NhlTeamDeserializer extends StdDeserializer<Team[]> {

    public NhlTeamDeserializer() {
        this(null);
    }

    public NhlTeamDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Team[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode teamsRootNode = jsonParser.getCodec().readTree(jsonParser);

        final var teams = new ArrayList<Team>();

        final var teamsNode = teamsRootNode.get("teams");

        for (final var teamNode : teamsNode) {
            final var team = new Team();
            team.setId(teamNode.get("id").toString());
            team.setFullName(teamNode.get("name").textValue());
            team.setCityName(teamNode.get("locationName").textValue());
            team.setNickName(teamNode.get("teamName").textValue());
            team.setAbbreviation(teamNode.get("abbreviation").textValue());

            if (teamNode.get("roster") != null) {
                final var rosterNode = teamNode.get("roster").get("roster");
                team.setRoster(parseRosterFromPlayersNode(rosterNode));
            }

            teams.add(team);
        }



        return teams.toArray(new Team[0]);
    }
}
