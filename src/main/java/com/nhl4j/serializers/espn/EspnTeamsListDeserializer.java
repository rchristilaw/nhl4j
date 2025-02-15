package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.nhl4j.domain.Team;

import java.io.IOException;
import java.util.ArrayList;

public class EspnTeamsListDeserializer extends StdDeserializer<Team[]> {

    private final ObjectMapper objectMapper;

    public EspnTeamsListDeserializer(ObjectMapper objectMapper) {
        this(null, objectMapper);
    }

    public EspnTeamsListDeserializer(Class<?> vc, ObjectMapper objectMapper) {
        super(vc);
        this.objectMapper = objectMapper;
    }

    @Override
    public Team[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        final var teamsNode = rootNode.get("sports").get(0).get("leagues").get(0).get("teams");

        final var teams = new ArrayList<Team>();

        for (final var teamNode : teamsNode) {
            teams.add(objectMapper.treeToValue(teamNode, Team.class));
        }

        return teams.toArray(new Team[0]);
    }
}
