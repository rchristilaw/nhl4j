package com.nhl4j.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfo {

    private String id;
    private String name;
    private String teamName;
    private String abbreviation;
    private List<Player> roster;

    @Builder
    public record Player(String id, String fullName, String position, String number) { }
}
