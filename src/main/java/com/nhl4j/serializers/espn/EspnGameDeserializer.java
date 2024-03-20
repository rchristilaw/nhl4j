package com.nhl4j.serializers.espn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nhl4j.domain.*;
import com.nhl4j.serializers.StatHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nhl4j.domain.Stat.*;
import static com.nhl4j.serializers.StatHelper.getMaxStat;
import static com.nhl4j.serializers.StatHelper.getSumOfPlayerStat;
import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseGameStatusFromCompetitionNode;
import static com.nhl4j.serializers.espn.EspnDeserializationHelper.parseTeamFromCompetitionNode;

public class EspnGameDeserializer extends StdDeserializer<Game> {

    private static final Map<String, List<Stat>> NFL_STAT_CATEGORIES = Map.of(
            "passing", List.of(PASSING_YARDS, PASSING_COMPLETIONS, PASSING_TDS, PASSING_INTS),
            "rushing", List.of(RUSHING_YARDS, RUSHING_ATTEMPTS, RUSHING_TDS, RUSHING_LONG),
            "receiving", List.of(RECEIVING_YARDS, RECEPTIONS, RECEIVING_TDS, RECEIVING_LONG),
            "kicking", List.of(FIELD_GOALS, FIELD_GOALS_LONG, KICKING_POINTS),
            "punting", List.of(PUNTS, PUNTS_LONG, PUNT_YARDS)
            );

    private static final Map<String, List<Stat>> NHL_STAT_CATEGORIES = Map.of(
            "forwards", List.of(GOALS, ASSISTS, GOALS_PP, GOALS_SH, PIMS, SOGS, HITS, FACEOFF_WINS),
            "defenses", List.of(GOALS, ASSISTS, GOALS_PP, GOALS_SH, PIMS, SOGS, HITS, FACEOFF_WINS),
            "goalies", List.of(SAVES)
    );

    public EspnGameDeserializer(ApiSource source) {
        this(null, source);
    }

    public EspnGameDeserializer(Class<?> vc, ApiSource apiSource) {
        super(vc);
        this.apiSource = apiSource;
    }

    private ApiSource apiSource;

    @Override
    public Game deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode gameNode = jsonParser.getCodec().readTree(jsonParser);

        final var competitionNode = gameNode.get("header").get("competitions").get(0);

        final var game = new Game();
        game.setId(competitionNode.get("id").textValue());
        final var dateString = competitionNode.get("date").textValue();
        game.setGameDate(dateString);

        final var sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            game.setDate(sdf.parse(dateString).toInstant());
        } catch (ParseException ignored) {
        }

        game.setGameStatus(parseGameStatusFromCompetitionNode(competitionNode));

        game.setHome(parseTeamFromCompetitionNode(competitionNode, "home", true));
        game.setAway(parseTeamFromCompetitionNode(competitionNode, "away", true));
        game.getHome().setRoster(new ArrayList<>());
        game.getAway().setRoster(new ArrayList<>());

        final var boxscoreNode = gameNode.get("boxscore");
        parseTeamStats(boxscoreNode, game.getHome());
        parseTeamStats(boxscoreNode, game.getAway());

        game.setBettingLine(BettingLine.parseLinesJson((ArrayNode) gameNode.get("pickcenter")));

        StatHelper.buildGameStats(game);

        if (apiSource == ApiSource.ESPN_NFL) {
            game.getStats().put(RECEIVING_LONG, getMaxStat(game, RECEIVING_LONG));
            game.getStats().put(RUSHING_LONG, getMaxStat(game, RUSHING_LONG));
            game.getStats().put(FIELD_GOALS_LONG, getMaxStat(game, FIELD_GOALS_LONG));
            game.getStats().put(PUNTS_LONG, getMaxStat(game, PUNTS_LONG));
        }

        return game;
    }

    private void parseTeamStats(JsonNode boxscoreNode, Team team) {
        if (boxscoreNode.get("players") != null) {
            parsePlayerStats(team, (ArrayNode) boxscoreNode.get("players"));
        }

        if (boxscoreNode.get("teams") != null) {
            for (final var teamsNode : boxscoreNode.get("teams")) {
                if (!teamsNode.get("team").get("id").textValue().equals(team.getId())) {
                    continue;
                }

                final var teamStats = team.getStats();

                final var statisticsNode = (ArrayNode)teamsNode.get("statistics");

                if (apiSource == ApiSource.ESPN_NFL) {
                    teamStats.put(INTERCEPTIONS, getTeamStat(INTERCEPTIONS, statisticsNode));
                    teamStats.put(FUMBLES, getTeamStat(FUMBLES, statisticsNode));
                    teamStats.put(TURNOVERS, getTeamStat(TURNOVERS, statisticsNode));

                    teamStats.put(FIELD_GOALS, getSumOfPlayerStat(team, FIELD_GOALS));
                    teamStats.put(KICKING_POINTS, getSumOfPlayerStat(team, KICKING_POINTS));
                    teamStats.put(PUNTS, getSumOfPlayerStat(team, PUNTS));
                    teamStats.put(PUNT_YARDS, getSumOfPlayerStat(team, PUNT_YARDS));
                    teamStats.put(RUSHING_TDS, getSumOfPlayerStat(team, RUSHING_TDS));
                    teamStats.put(PASSING_TDS, getSumOfPlayerStat(team, PASSING_TDS));

                    // Need to parse out the sack yards value
                    final var sacks = getTeamStat(SACKS, statisticsNode).split("-")[0];
                    teamStats.put(SACKS, sacks);
                } else if (apiSource == ApiSource.ESPN_NHL) {
                    teamStats.put(GOALS_PP, getTeamStat(GOALS_PP, statisticsNode));
                    teamStats.put(GOALS_SH, getTeamStat(GOALS_SH, statisticsNode));
                    teamStats.put(PIMS, getTeamStat(PIMS, statisticsNode));
                    teamStats.put(SOGS, getTeamStat(SOGS, statisticsNode));
                    teamStats.put(HITS, getTeamStat(HITS, statisticsNode));
                    teamStats.put(FACEOFF_WINS, getTeamStat(FACEOFF_WINS, statisticsNode));
                }

            }
        }
    }

    private String getTeamStat(Stat stat, ArrayNode statisticsNode)  {
        final var statNode = getStatsNodeByType(statisticsNode, stat.getKey());
        return statNode != null ? statNode.get("displayValue").textValue() : "0";
    }

    private void parsePlayerStats(Team team, ArrayNode teamPlayerStatsNode) {
        for (final var playerStatsNode : teamPlayerStatsNode) {
            if (!playerStatsNode.get("team").get("id").textValue().equals(team.getId())) {
                continue;
            }

            final var categories = apiSource == ApiSource.ESPN_NFL
                    ? NFL_STAT_CATEGORIES : NHL_STAT_CATEGORIES;

            for (final var statCategory : categories.entrySet()) {
                final var categoryNode = getStatsNodeByType(
                        (ArrayNode)playerStatsNode.get("statistics"), statCategory.getKey());

                if (categoryNode == null) {
                    continue;
                }

                final var keys = (ArrayNode)categoryNode.get("keys");

                for (int i = 0; i < keys.size(); i++) {
                    final var key = keys.get(i);

                    final var statKey = statCategory.getValue().stream()
                            .filter(it -> it.getKey().equals(key.textValue()))
                            .findFirst().orElse(null);

                    if (statKey == null) {
                        continue;
                    }

                    final var athletesNode = (ArrayNode)categoryNode.get("athletes");
                    for (final var athleteNode : athletesNode) {
                        final var statValue = athleteNode.get("stats").get(i).textValue();
                        addStatToPlayer(team, athleteNode.get("athlete"), statKey, statValue);
                    }
                }

            }
        }
    }

    private JsonNode getStatsNodeByType(ArrayNode statsNodes, String statType) {
        for (final var statsNode : statsNodes) {
            if (statType.equals(statsNode.get("name").textValue())) {
                return statsNode;
            }
        }
        return null;
    }

    private void addStatToPlayer(Team team, JsonNode playerNode, Stat stat, String value) {
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
