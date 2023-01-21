package com.nhl4j.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Player {

    private String id;
    private String fullName;
    private int goals;
    private int assists;
    private int penaltyMins;
    private int shotsOnGoal;

    private Map<Stat, String> stats = new HashMap<>();
}
