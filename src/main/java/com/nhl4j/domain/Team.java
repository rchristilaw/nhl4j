package com.nhl4j.domain;

import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Team {
    private String id;
    private String fullName;
    private String cityName;
    private String nickName;
    private String abbreviation;

    List<Player> roster;

    private Map<Stat, String> stats;
}
