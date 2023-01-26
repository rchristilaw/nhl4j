package com.nhl4j.serializers.nhl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NhlDeserializationHelper {

    public static List<Player> parseRosterFromPlayersNode(JsonNode playersNode) {
        final var playerList = new ArrayList<Player>();

        final var playerElements = playersNode.elements();

        while (playerElements.hasNext()) {
            final var playerNode = playerElements.next();
            playerList.add(parsePlayerFromPlayerNode(playerNode));
        }

        return playerList;
    }
    private static Player parsePlayerFromPlayerNode(JsonNode playerNode) {
        final var player = new Player();

        final var playerInfo = playerNode.get("person");
        player.setId(playerInfo.get("id").toString());
        player.setFullName(playerInfo.get("fullName").textValue());

        final var primaryNumberNode = playerInfo.get("primaryNumber");
        final var jerseyNumberNode = playerNode.get("jerseyNumber");
        if (primaryNumberNode != null) {
            player.setNumber(primaryNumberNode.textValue());
        } else {
            player.setNumber(jerseyNumberNode.textValue());
        }

        final var primaryPositionNode = playerInfo.get("primaryPosition");
        final var positionNode = playerNode.get("position");
        if (primaryPositionNode != null) {
            player.setPosition(primaryPositionNode.get("abbreviation").textValue());
        } else {
            player.setPosition(positionNode.get("abbreviation").textValue());
        }

        final var statsNode = playerNode.get("stats");
        if (statsNode != null && statsNode.get("skaterStats") != null) {
            final var skaterStatsNode = statsNode.get("skaterStats");
            final var stats = new HashMap<Stat, String>();
            stats.put(Stat.GOALS, skaterStatsNode.get("goals").toString());
            stats.put(Stat.GOALS_PP, skaterStatsNode.get("powerPlayGoals").toString());
            stats.put(Stat.GOALS_SH, skaterStatsNode.get("shortHandedGoals").toString());
            stats.put(Stat.ASSISTS, skaterStatsNode.get("assists").toString());
            stats.put(Stat.PIMS, skaterStatsNode.get("penaltyMinutes").toString());
            stats.put(Stat.SOGS, skaterStatsNode.get("shots").toString());
            stats.put(Stat.HITS, skaterStatsNode.get("hits").toString());
            stats.put(Stat.FACEOFF_WINS, skaterStatsNode.get("faceOffWins").toString());
            player.setStats(stats);
        }

        return player;
    }
}
