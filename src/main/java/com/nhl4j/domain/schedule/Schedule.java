package com.nhl4j.domain.schedule;

import com.nhl4j.domain.game.Game;
import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    private int totalGames;
    private List<Game> games;

//    String dates;
}
