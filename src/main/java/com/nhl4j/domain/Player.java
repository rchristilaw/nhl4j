package com.nhl4j.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    private String id;
    private String fullName;
    private String position;
    private String number;

    @Builder.Default
    private Map<Stat, String> stats = new HashMap<>();
}
