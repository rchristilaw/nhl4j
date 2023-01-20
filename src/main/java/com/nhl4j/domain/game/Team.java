package com.nhl4j.domain.game;

import com.nhl4j.domain.Player;
import lombok.Data;

import java.util.List;

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

    List<Player> players;
}
