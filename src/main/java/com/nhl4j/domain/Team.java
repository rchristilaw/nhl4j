package com.nhl4j.domain;

import lombok.Data;

import java.util.ArrayList;
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

    private List<String> lineScore = new ArrayList<>();

    private Map<Stat, String> stats = new HashMap<>();
}
