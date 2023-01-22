package com.nhl4j;


import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class NflApi {

    private final RestClient restClient;

    private static final String BASE_URL = "https://site.api.espn.com/apis/site/v2/sports/football/nfl/";

    public NflApi(RestTemplate restTemplate) {
        this.restClient = new RestClient(restTemplate, League.NFL);
    }

    public List<Team> getTeams() throws StatsApiException {
        try {
            return Arrays.asList(restClient.doGet(BASE_URL + "teams", Team[].class));
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the list of teams", ex);
        }
    }

    //https://site.api.espn.com/apis/site/v2/sports/football/nfl/teams/22
    public Team getTeam(String teamId) throws StatsApiException {
        try {
            final var path = String.format("teams/%s", teamId);
            return restClient.doGet(BASE_URL + path, Team.class);
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
            return restClient.doGet(url, Schedule.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the schedule", ex);
        }
    }

    //https://site.api.espn.com/apis/site/v2/sports/football/nfl/summary?event=401438002
    public Game getGameDetails(String gameId) throws StatsApiException {
        try {
            String url = BASE_URL + "summary?event=" + gameId;
            return restClient.doGet(url, Game.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch game: " + gameId, ex);
        }
    }
}
