package com.nhl4j.domain;

import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    private int totalGames;
    private List<Game> games;
}
