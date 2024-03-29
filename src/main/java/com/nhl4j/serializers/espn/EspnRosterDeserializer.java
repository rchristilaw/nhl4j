package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.Player;

import java.io.IOException;
import java.util.ArrayList;

public class EspnRosterDeserializer extends StdDeserializer<Player[]> {

    public EspnRosterDeserializer() {
        this(null);
    }

    public EspnRosterDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Player[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode rosterNode = jsonParser.getCodec().readTree(jsonParser);

        final var roster = new ArrayList<Player>();

        final var athletesNode = (ArrayNode)rosterNode.get("athletes");

        for (final var athleteItemsNode : athletesNode) {
            final var playersNode = athleteItemsNode.get("items");

            for (final var playerNode : playersNode) {
                final var player = new Player();
                player.setId(playerNode.get("id").textValue());
                player.setFullName(playerNode.get("fullName").textValue());
                player.setPosition(playerNode.get("position").get("abbreviation").textValue());
                player.setNumber(playerNode.get("jersey") != null ? playerNode.get("jersey").textValue() : "0");

                roster.add(player);
            }
        }

        return roster.toArray(new Player[0]);
    }
}
