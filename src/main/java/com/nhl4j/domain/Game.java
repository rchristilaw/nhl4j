package com.nhl4j.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Game {
    private String id;
    private String gameDate;
    private GameStatus gameStatus;
    private Team home;
    private Team away;

    private Map<Stat, String> stats = new HashMap<>();
}
