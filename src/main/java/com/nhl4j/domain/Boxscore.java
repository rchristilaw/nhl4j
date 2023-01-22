package com.nhl4j.domain;

import lombok.Data;

@Data
public class Boxscore {
    private String id;
    private String gameDate;
    private GameStatus gameStatus;
    private Team home;
    private Team away;
}
