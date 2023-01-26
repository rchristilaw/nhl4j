package com.nhl4j.serializers;

import com.nhl4j.domain.Game;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;

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
        return total % 1 == 0 ? String.valueOf((int)total) : String.valueOf(total);
    }
}
