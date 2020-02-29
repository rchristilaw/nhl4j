package com.nhl4j.domain.game;

import lombok.Data;

@Data
public class GameStatus {
    private String statusCode;
    private String detailedState;
}
