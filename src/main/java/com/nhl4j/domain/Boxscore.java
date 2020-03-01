package com.nhl4j.domain;

import com.nhl4j.domain.game.Team;
import lombok.Data;

@Data
public class Boxscore {
    private Team homeTeam;
    private Team awayTeam;
}
