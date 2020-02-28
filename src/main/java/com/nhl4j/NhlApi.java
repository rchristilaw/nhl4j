package com.nhl4j;


import com.nhl4j.domain.TeamsData;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class NhlApi {

    private final RestClient restClient;

    private static final String BASE_URL = "https://statsapi.web.nhl.com/api/v1/";


    public NhlApi(RestTemplate restTemplate) {
        this.restClient = new RestClient(restTemplate);
    }

    public TeamsData getTeams() {
        try {
            TeamsData teamsData = restClient.doGet(BASE_URL + "teams", TeamsData.class);
            return teamsData;
        } catch (Exception ex) {
            return null;
        }
    }
}
