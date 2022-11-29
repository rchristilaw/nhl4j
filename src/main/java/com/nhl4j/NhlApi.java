package com.nhl4j;


import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.TeamsData;
import com.nhl4j.domain.schedule.Schedule;
import com.nhl4j.exception.NhlApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class NhlApi {

    private final RestClient restClient;

    private static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1/";

    public NhlApi(RestTemplate restTemplate) {
        this.restClient = new RestClient(restTemplate);
    }

    public TeamsData getTeams() throws NhlApiException {
        try {
            return restClient.doGet(BASE_URL + "teams", TeamsData.class);
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch the list of teams", ex);
        }
    }

    public TeamsData getTeam(int teamId) throws NhlApiException {
        try {
            final var path = String.format("teams/%s?expand=team.roster", teamId);
            return restClient.doGet(BASE_URL + path, TeamsData.class);
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch team", ex);
        }
    }

    public Schedule getScheduleForDate(Date date) throws NhlApiException {
        String formattedDate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.setTimeZone(TimeZone.getTimeZone("UTC"));
            formattedDate = formatter.format(cal.getTime());
        } catch (Exception ex) {
            throw new NhlApiException("Failed to parse date", ex);
        }

        try {
            String url = BASE_URL + "schedule?date=" + formattedDate;
            return restClient.doGet(url, Schedule.class);
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch the schedule", ex);
        }
    }

    public Boxscore getGameBoxscore(int id) throws NhlApiException {
        try {
            String url = BASE_URL + "game/" + id + "/boxscore";
            return restClient.doGet(url, Boxscore.class);
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch the schedule", ex);
        }
    }
}
