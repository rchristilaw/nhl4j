package com.nhl4j.domain.schedule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nhl4j.domain.game.Game;
import com.nhl4j.serializers.ScheduleDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class Schedule {
    private int totalGames;
    private List<Game> games;

//    String dates;
}
