package com.nhl4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private int id;
    private String name;
    private String teamName;
    private String abbreviation;
}
