package com.nhl4j.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Schedule {

    private List<Game> games = new ArrayList<>();

    public int getTotalGames() {
        return games.size();
    }
}
