package com.nhl4j.domain.game;

import lombok.Data;

@Data
public class Game {
    private int id;
    private String gameDate;
    private GameStatus gameStatus;
    private Team home;
    private Team away;
}
