package com.nhl4j;


import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class NflApi extends BaseApi{

    private static final String BASE_URL = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/";

    public NflApi(RestTemplate restTemplate) {
        super(restTemplate, ApiSource.ESPN_NFL);
    }

    public List<Team> getTeams() throws StatsApiException {
        try {
            return Arrays.asList(doGet(BASE_URL + "teams", Team[].class));
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the list of teams", ex);
        }
    }

    //https://site.api.espn.com/apis/site/v2/sports/football/nfl/teams/22
    public Team getTeam(String teamId) throws StatsApiException {
        try {
            final var teamPath = String.format("%s/teams/%s", BASE_URL, teamId);

            final var team = doGet(teamPath, Team.class);

            final var roster = Arrays.asList(doGet(teamPath + "/roster", Player[].class));
            team.setRoster(roster);

            return team;
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch team: " + teamId, ex);
        }
    }

    //https://site.api.espn.com/apis/site/v2/sports/football/nfl/scoreboard?dates=20230121
    public Schedule getScheduleForDate(Date date) throws StatsApiException {
        String formattedDate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            formattedDate = formatter.format(cal.getTime());
        } catch (Exception ex) {
            throw new StatsApiException("Failed to parse date", ex);
        }

        try {
            String url = BASE_URL + "scoreboard?dates=" + formattedDate;
            return doGet(url, Schedule.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the schedule", ex);
        }
    }

    //https://site.api.espn.com/apis/site/v2/sports/football/nfl/summary?event=401671795
    public Game getGameDetails(String gameId) throws StatsApiException {
        try {
            String url = BASE_URL + "summary?event=" + gameId;
            return doGet(url, Game.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch game: " + gameId, ex);
        }
    }
}
