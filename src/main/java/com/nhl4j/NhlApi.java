package com.nhl4j;


import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class NhlApi {

    private final RestClient restClient;

    private static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1/";

    public NhlApi(RestTemplate restTemplate) {
        this.restClient = new RestClient(restTemplate, League.NHL);
    }

    public List<Team> getTeams() throws StatsApiException {
        try {
            return Arrays.asList(restClient.doGet(BASE_URL + "teams", Team[].class));
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the list of teams", ex);
        }
    }

    public Team getTeam(String teamId) throws StatsApiException {
        try {
            final var path = String.format("teams/%s?expand=team.roster", teamId);
            return restClient.doGet(BASE_URL + path, Team[].class)[0];
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch team", ex);
        }
    }

    public Schedule getScheduleForDate(Date date) throws StatsApiException {
        String formattedDate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            formattedDate = formatter.format(cal.getTime());
        } catch (Exception ex) {
            throw new StatsApiException("Failed to parse date", ex);
        }

        try {
            String url = BASE_URL + "schedule?date=" + formattedDate;
            return restClient.doGet(url, Schedule.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the schedule", ex);
        }
    }

    public Game getGameDetails(String gameId) throws StatsApiException {
        try {
            String url = BASE_URL + "game/" + gameId + "/feed/live";
            return restClient.doGet(url, Game.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch game: " + gameId, ex);
        }
    }
}
