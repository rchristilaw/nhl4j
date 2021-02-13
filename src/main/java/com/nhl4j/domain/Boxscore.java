package com.nhl4j.domain;

import com.nhl4j.domain.game.GameStatus;
import com.nhl4j.domain.game.Team;
import lombok.Data;

@Data
public class Boxscore {
    private int id;
    private String gameDate;
    private GameStatus gameStatus;
    private Team homeTeam;
    private Team awayTeam;
}
