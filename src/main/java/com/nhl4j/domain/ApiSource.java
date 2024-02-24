package com.nhl4j.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiSource {
    NHL("NHL"),
    ESPN_NFL("ESPN"),
    ESPN_NHL("ESPN");

    private String apiSite;

    public boolean isEspn() {
        return "ESPN".equals(apiSite);
    }
}
