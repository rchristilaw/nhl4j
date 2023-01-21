package com.nhl4j.domain.game;

import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Team {
    private String id;
    private String name;
    private String shortName;
    private String abbreviation;
    private int score;
    private int penaltyMinutes;
    private int shotsOnGoal;
    private int hits;

    private Map<Stat, String> stats = new HashMap<>();

    List<Player> players;
}
