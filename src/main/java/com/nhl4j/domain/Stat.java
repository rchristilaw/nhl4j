package com.nhl4j.domain;

import lombok.Getter;

@Getter
public enum Stat {
    // All
    SCORE("points"),
    SCORE_Q1("1st quarter points"),
    SCORE_Q2("2nd quarter points"),
    SCORE_Q3("3rd quarter points"),
    SCORE_Q4("4th quarter points"),

    // Hockey
    GOALS("goals"),
    GOALS_PP("power play goals"),
    GOALS_SH("short handed goals"),
    ASSISTS("assists"),
    PIMS("penalty minutes"),
    SOGS("shots on goal"),
    HITS("hits"),
    FACEOFF_WINS("faceoff wins"),

    // Football
    PASSING_YARDS("passing yards", "passingYards"),
    PASSING_COMPLETIONS("Comp/Attempts", "completions/passingAttempts"),
    PASSING_TDS("passing TDs", "passingTouchdowns"),
    PASSING_INTS("interceptions", "interceptions"),

    RUSHING_YARDS("rushing yards", "rushingYards"),
    RUSHING_ATTEMPTS("rushing attempts", "rushingAttempts"),
    RUSHING_TDS("rushing TDs", "rushingTouchdowns"),
    RUSHING_LONG("longest rush", "longRushing"),

    RECEIVING_YARDS("receiving yards", "receivingYards"),
    RECEPTIONS("receptions", "receptions"),
    RECEIVING_TDS("receiving TDs", "receivingTouchdowns"),
    RECEIVING_LONG("longest reception", "longReception"),

    FIELD_GOALS("field goals", "fieldGoalsMade/fieldGoalAttempts"),
    FIELD_GOALS_LONG("longest field goal", "longFieldGoalMade"),
    KICKING_POINTS("kicking points", "totalKickingPoints"),

    PUNTS("punts", "punts"),
    PUNTS_LONG("longest punt", "longPunt"),
    PUNT_YARDS("punt yards", "puntYards"),

    SACKS("sacks", "sacksYardsLost"),
    FUMBLES("fumbles lost", "fumblesLost"),
    INTERCEPTIONS("interceptions", "interceptions"),
    TURNOVERS("turnovers", "turnovers");

    Stat(String name) {
        this.name = name;
        this.key = "";
    }

    Stat(String name, String key) {
        this.name = name;
        this.key = key;
    }

    private final String name;
    private final String key;

    public boolean isSplitStat() {
        return key.contains("/");
    }
}
