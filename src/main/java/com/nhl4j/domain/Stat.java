package com.nhl4j.domain;

import lombok.Getter;

@Getter
public enum Stat {
    // All
    SCORE("total score"),

    // Hockey
    GOALS("goals"),
    ASSISTS("assists"),
    PIMS("penalty minutes"),
    SOGS("shots on goal"),
    HITS("hits"),

    // Football
    PASSING_YARDS("passing yards", "passingYards"),
    PASSING_COMPLETIONS_ATTEMPTS("Comp/Attempts", "completions/passingAttempts"),
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

    FUMBLES("fumbles"),
    INTERCEPTIONS("interceptions");

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
}
