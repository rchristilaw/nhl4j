package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Team;

import java.io.IOException;

public class EspnTeamDeserializer extends StdDeserializer<Team> {

    public EspnTeamDeserializer() {
        this(null);
    }

    public EspnTeamDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Team deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        final JsonNode teamNode = jsonParser.getCodec().readTree(jsonParser);


        final var teamDataNode = teamNode.get("team");
        final var team = new Team();

        team.setId(teamDataNode.get("id").textValue());
        team.setFullName(teamDataNode.get("displayName").textValue());
        team.setCityName(teamDataNode.get("location").textValue());
        team.setNickName(teamDataNode.get("name").textValue());
        team.setAbbreviation(teamDataNode.get("abbreviation").textValue());

        return team;
    }
}
