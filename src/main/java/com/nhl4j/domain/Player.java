package com.nhl4j.domain;

import lombok.Data;

@Data
public class Player {

    private int id;
    private String fullName;
    private int goals;
    private int assists;
    private int penaltyMins;
    private int shotsOnGoal;
}
