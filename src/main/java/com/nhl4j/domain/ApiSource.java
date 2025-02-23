package com.nhl4j.domain;

import com.nhl4j.serializers.espn.statparser.BaseballStatParser;
import com.nhl4j.serializers.espn.statparser.FootballStatParser;
import com.nhl4j.serializers.espn.statparser.HockeyStatParser;
import com.nhl4j.serializers.espn.statparser.StatParser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiSource {

    NHL("NHL", null, null, null, null, null),
    ESPN_MLB("ESPN", new BaseballStatParser(), "https://site.api.espn.com/apis/site/v2/sports/baseball/mlb",
            "/teams", "scoreboard?dates=", "/summary?event="),
    ESPN_NFL("ESPN", new FootballStatParser(), null, null, null, null),
    ESPN_NHL("ESPN", new HockeyStatParser(), null, null, null, null);

    private final String apiSite;
    private final StatParser statParser;
    private final String baseUrl;
    private final String teamsPath;
    private final String schedulePath;
    private final String boxscorePath;

    public boolean isEspn() {
        return "ESPN".equals(apiSite);
    }
}
