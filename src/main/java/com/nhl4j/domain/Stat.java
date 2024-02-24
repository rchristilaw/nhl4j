package com.nhl4j.domain;

import lombok.Getter;

@Getter
public enum Stat {
    // All
    SCORE,
    SCORE_Q1,
    SCORE_Q2,
    SCORE_Q3,
    SCORE_Q4,

    // Hockey
    GOALS("goals"),
    ASSISTS("assists"),
    GOALS_PP("powerPlayGoals"),
    GOALS_SH("shortHandedGoals"),
    PIMS("penaltyMinutes"),
    SOGS("shotsTotal"),
    HITS("hits"),
    FACEOFF_WINS("faceoffsWon"),
    SAVES("saves"),

    // Football
    PASSING_YARDS("passingYards"),
    PASSING_COMPLETIONS("completions/passingAttempts"),
    PASSING_TDS("passingTouchdowns"),
    PASSING_INTS("interceptions"),

    RUSHING_YARDS("rushingYards"),
    RUSHING_ATTEMPTS("rushingAttempts"),
    RUSHING_TDS("rushingTouchdowns"),
    RUSHING_LONG("longRushing"),

    RECEIVING_YARDS("receivingYards"),
    RECEPTIONS("receptions"),
    RECEIVING_TDS("receivingTouchdowns"),
    RECEIVING_LONG("longReception"),

    FIELD_GOALS("fieldGoalsMade/fieldGoalAttempts"),
    FIELD_GOALS_LONG("longFieldGoalMade"),
    KICKING_POINTS("totalKickingPoints"),

    PUNTS("punts"),
    PUNTS_LONG("longPunt"),
    PUNT_YARDS("puntYards"),

    SACKS("sacksYardsLost"),
    FUMBLES("fumblesLost"),
    INTERCEPTIONS("interceptions"),
    TURNOVERS("turnovers");

    Stat() {
        this.key = "";
    }

    Stat(String key) {
        this.key = key;
    }
    private final String key;

    public boolean isSplitStat() {
        return key.contains("/");
    }
}
