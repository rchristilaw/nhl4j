package com.nhl4j;


import com.nhl4j.domain.Game;
import com.nhl4j.domain.ApiSource;
import com.nhl4j.domain.Schedule;
import com.nhl4j.domain.Team;
import com.nhl4j.exception.StatsApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

public class NhlApiV1 extends BaseApi {

    private static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1/";

    public NhlApiV1(RestTemplate restTemplate) {
        super(restTemplate, ApiSource.NHL);
    }

    public List<Team> getTeams() throws StatsApiException {
        try {
            return Arrays.asList(doGet(BASE_URL + "teams", Team[].class));
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the list of teams", ex);
        }
    }

    public Team getTeam(String teamId) throws StatsApiException {
        try {
            final var path = String.format("teams/%s?expand=team.roster", teamId);
            return doGet(BASE_URL + path, Team[].class)[0];
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
            return doGet(url, Schedule.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch the schedule", ex);
        }
    }

    public Game getBoxscore(String gameId) throws StatsApiException {
        try {
            String url = BASE_URL + "game/" + gameId + "/feed/live";
            return doGet(url, Game.class);
        } catch (Exception ex) {
            throw new StatsApiException("Failed to fetch game: " + gameId, ex);
        }
    }
}
