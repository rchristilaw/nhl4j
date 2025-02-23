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
    TDS,
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
    TURNOVERS("turnovers"),

    // Baseball
    MLB_K("strikeouts"),
    MLB_RBI("RBIs"),
    MLB_HITS("hits"),
    MLB_RUNS("runs"),
    MLB_DOUBLES("doubles"),
    MLB_TRIPLES("triples"),
    MLB_HR("homeRuns"),
    MLB_GRAND_SLAMS("grandSlamHmeRuns"),
    MLB_XBH("extraBaseHits"),
    MLB_TB("totalBases"),
    MLB_SB("stolenBases"),
    MLB_AB("atBats"),
    MLB_BB("walks"),
    MLB_HBP("hitByPitch"),
    MLB_GIDP("GIDPs"),
    MLB_RUNNER_LOB("runnersLeftOnBase"),
    MLB_CAUGHT_STEALING("caughtStealing"),

    // Pitching
    MLB_PITCHER_K("strikeouts"),
    MLB_PITCHER_BB("walks"),
    MLB_PITCHER_RBI("RBIs"),
    MLB_PITCHER_HITS("hits"),
    MLB_PITCHER_RUNS("runs"),
    MLB_PITCHER_DOUBLES("doubles"),
    MLB_PITCHER_TRIPLES("triples"),
    MLB_PITCHER_HR("homeRuns"),
    MLB_PITCHER_WILD_PITCHES("wildPitches"),
    MLB_PITCHER_BALKS("balks"),
    MLB_PITCHER_INTENTIONAL_BB("intentionalWalks"),
    MLB_PITCHER_HBP("battersHit"),
    MLB_PITCHER_IP("inningsPitched"),
    MLB_PITCHER_BATTERS_FACED("battersFaced"),
    MLB_PITCHER_OPPONENTS_TB("opponentsTotalBases"),
    MLB_PITCHER_PITCHES("ptiches"),
    MLB_PITCHER_SAVES("saves"),
    MLB_PITCHER_BLOWN_SAVES("blownSaves"),
    MLB_PITCHER_SHUTOUTS("shutouts"),
    MLB_PITCHER_W("wins"),
    MLB_PITCHER_L("losses"),

    MLB_ERRORS("errors"),
    MLB_OUTFIELD_ASSISTS("outfieldAssists"),
    MLB_DOUBLE_PLAYS("doublePlays"),
    MLB_TRIPLE_PLAYS("triplePlays"),

    OTHER;

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
