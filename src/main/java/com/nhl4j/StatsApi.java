package com.nhl4j;


import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class StatsApi {

    private final MlbApi mlbApi;
    private final NhlApi nhlApi;
    private final NflApi nflApi;

    public StatsApi(RestTemplate restTemplate) {
        mlbApi = new MlbApi(restTemplate);
        nhlApi = new NhlApi(restTemplate);
        nflApi = new NflApi(restTemplate);
    }

    private BaseApi getApiBySport(String sport) throws StatsApiException {
        final Sport apiSport;
        try {
            apiSport = Sport.valueOf(sport);
        } catch (IllegalArgumentException ex) {
            throw new StatsApiException(String.format("Could not fetch sport data. Unrecognized sport %s", sport));
        }

        switch (apiSport) {
            case HOCKEY -> {
                return nhlApi;
            }
            case BASEBALL -> {
                return mlbApi;
            }
            case FOOTBALL -> {
                return nflApi;
            }
            default -> throw new StatsApiException(String.format("Sport %s is currently unsupported", apiSport));
        }
    }

    public List<Team> getTeams(String sport) throws StatsApiException {
        return getApiBySport(sport).getTeams();
    }

    public Team getTeam(String sport, String teamId) throws StatsApiException {
        return getApiBySport(sport).getTeam(teamId);
    }

    public Schedule getScheduleForDate(String sport, Date date) throws StatsApiException {
        return getApiBySport(sport).getScheduleForDate(date);
    }

    public Game getBoxscore(String sport, String gameId) throws StatsApiException {
        return getApiBySport(sport).getBoxscore(gameId);
    }

}
