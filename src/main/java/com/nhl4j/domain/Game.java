package com.nhl4j.domain;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Game {
    private String id;
    private String gameDate;
    private Instant date;
    private GameStatus gameStatus;
    private Team home;
    private Team away;

    private BettingLine bettingLine;

    private Map<Stat, String> stats = new HashMap<>();
    private List<Event> events = new ArrayList<>();

    public record Event(String sequence, int period, String periodTime,
                        Stat stat, String teamId, List<String> playerIds) {};
}
