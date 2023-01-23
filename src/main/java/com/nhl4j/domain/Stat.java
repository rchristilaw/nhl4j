package com.nhl4j.domain;

import lombok.Getter;

@Getter
public enum Stat {
    // All
    SCORE,

    // Hockey
    GOALS,
    ASSISTS,
    PIMS,
    SOGS,
    HITS,

    // Football
    PASSING_YARDS("passingYards"),
    PASSING_COMPLETIONS_ATTEMPTS("completions/passingAttempts"),
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

    FUMBLES,
    INTERCEPTIONS;

    Stat() {
        this.key = "";
    }

    Stat(String key) {
        this.key = key;
    }

    private String key;
}
