package com.nhl4j.serializers;

import com.nhl4j.domain.Game;
import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;

import java.util.ArrayList;

public class StatHelper {

    public static void buildGameStats(Game game) {
        final var home = game.getHome();
        final var away = game.getAway();

        for (final var stat : home.getStats().keySet()) {
            game.getStats().put(stat, addStats(home, away, stat));
        }
    }

    private static String addStats(Team team1, Team team2, Stat stat) {
        final var stat1 = team1.getStats().get(stat);
        final var stat2 = team2.getStats().get(stat);

        final var statValue1 = stat1 != null ? Float.parseFloat(stat1) : 0;
        final var statValue2 = stat2 != null ? Float.parseFloat(stat2) : 0;

        final var total = statValue1 + statValue2;
        return normalizeValue(total);
    }

    public static String getSumOfPlayerStat(Team team, Stat stat) {
        final var players = team.getRoster();

        float sum = 0;
        for (final var player : players) {
            if (player.getStats().containsKey(stat)) {
                sum += Float.parseFloat(player.getStats().get(stat));
            }
        }

        return normalizeValue(sum);
    }

    public static String getMaxStat(Game game, Stat stat) {

        final var allPlayers = new ArrayList<Player>();
        allPlayers.addAll(game.getHome().getRoster());
        allPlayers.addAll(game.getAway().getRoster());

        float maxValue = 0;
        for (final var player : allPlayers) {
            if (player.getStats().containsKey(stat)) {
                final var currentValue = Float.parseFloat(player.getStats().get(stat));
                maxValue = Float.max(currentValue, maxValue);
            }
        }

        return normalizeValue(maxValue);
    }

    private static String normalizeValue(float value) {
        return value % 1 == 0 ? String.valueOf((int)value) : String.valueOf(value);
    }
}