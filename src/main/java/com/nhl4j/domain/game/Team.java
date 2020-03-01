package com.nhl4j.domain.game;

import com.nhl4j.domain.Player;
import lombok.Data;

import java.util.List;

@Data
public class Team {
    private int id;
    private String name;

    private int score;
    private int penaltyMinutes;
    private int shotsOnGoal;
    private int hits;

    List<Player> players;
}
