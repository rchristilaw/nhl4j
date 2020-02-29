package com.nhl4j;


import com.nhl4j.domain.Schedule;
import com.nhl4j.domain.TeamsData;
import com.nhl4j.domain.game.Game;
import com.nhl4j.exception.NhlApiException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NhlApi {

    private final RestClient restClient;

    private static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1/";


    public NhlApi(RestTemplate restTemplate) {
        this.restClient = new RestClient(restTemplate);
    }

    public TeamsData getTeams() throws NhlApiException {
        try {
            TeamsData teamsData = restClient.doGet(BASE_URL + "teams", TeamsData.class);
            return teamsData;
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch the list of teams", ex);
        }
    }

    public Schedule getScheduleForDate(Date date) throws NhlApiException {
        String formattedDate;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = formatter.format(date);
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

    public String getGame(int id) throws NhlApiException {
        try {
            String url = BASE_URL + "game/" + id + "/boxscore";
            return restClient.doGet(url, String.class);
        } catch (Exception ex) {
            throw new NhlApiException("Failed to fetch the schedule", ex);
        }
    }
}
