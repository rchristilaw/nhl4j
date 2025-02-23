package com.nhl4j.serializers.espn.statparser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.*;

import java.util.List;
import java.util.Map;


public abstract class StatParser {

    public abstract void parseGameStats(Game game);
    public abstract void parseTeamStats(Team team, JsonNode teamNode);
    public abstract void parsePlayerStats(Team team, JsonNode playerStatsNode);
    public abstract void parseGameEvents(Game game, JsonNode gameNode);

    public void parseBettingLine(Game game, JsonNode gameNode) {
        game.setBettingLine(BettingLine.parseLinesJson((ArrayNode) gameNode.get("pickcenter")));
    }

    protected String getTeamStat(Stat stat, ArrayNode statisticsNode) {
        final var statNode = getStatsNodeByType(statisticsNode, stat.getKey());
        return statNode != null ? statNode.get("displayValue").textValue() : "0";
    }

    protected JsonNode getStatsNodeByType(ArrayNode statsNodes, String statType) {
        for (final var statsNode : statsNodes) {
            if (statType.equals(statsNode.get("name").textValue())) {
                return statsNode;
            }
        }
        return null;
    }

    protected void parsePlayerStats(Team team, JsonNode playerStatsNode,
                                    Map<String, List<Stat>> playerStatCategories) {
        for (final var statCategory : playerStatCategories.entrySet()) {

        }
    }

    protected void parsePlayerStatCategory(Team team, JsonNode categoryNode, List<Stat> categoryStats) {
        if (categoryNode == null) {
            return;
        }

        final var keys = (ArrayNode) categoryNode.get("keys");

        for (int i = 0; i < keys.size(); i++) {
            final var key = keys.get(i);

            final var statKey = categoryStats.stream()
                    .filter(it -> it.getKey().equals(key.textValue()))
                    .findFirst().orElse(null);

            if (statKey == null) {
                continue;
            }

            final var athletesNode = (ArrayNode) categoryNode.get("athletes");
            for (final var athleteNode : athletesNode) {
                final var statValue = athleteNode.get("stats").get(i).textValue();
                addStatToPlayer(team, athleteNode.get("athlete"), statKey, statValue);
            }
        }
    }

    protected void addStatToPlayer(Team team, JsonNode playerNode, Stat stat, String value) {
        final var currentPlayer = parsePlayerNode(playerNode);

        final var player = team.getRoster().stream()
                .filter(it -> it.getId().equals(currentPlayer.getId()))
                .findFirst().orElse(null);

        if (stat.isSplitStat()) {
            value = value.split("/")[0];
        }

        if (player != null) {
            player.getStats().put(stat, value);
        } else {
            currentPlayer.getStats().put(stat, value);
            team.getRoster().add(currentPlayer);
        }
    }

    private Player parsePlayerNode(JsonNode playerNode) {
        final var player = new Player();
        player.setId(playerNode.get("id").textValue());
        player.setFullName(playerNode.get("displayName").textValue());

        return player;
    }
}
