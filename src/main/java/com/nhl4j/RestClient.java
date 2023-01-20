package com.nhl4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.League;
import com.nhl4j.domain.TeamInfo;
import com.nhl4j.domain.schedule.Schedule;
import com.nhl4j.domain.game.Game;
import com.nhl4j.serializers.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final League league;

    public RestClient(RestTemplate restTemplate, League league) {
        this.restTemplate = restTemplate;
        this.objectMapper = configureObjectMapper(league);
        this.league = league;
    }

    private ObjectMapper configureObjectMapper(League league) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Game.class, new GameDeserializer(objectMapper));

        final var scheduleDeserializer = league == League.NFL
                ? new NflScheduleDeserializer(objectMapper) : new NhlScheduleDeserializer(objectMapper);
        module.addDeserializer(Schedule.class, scheduleDeserializer);
        module.addDeserializer(Boxscore.class, new BoxscoreDeserializer(objectMapper));
        module.addDeserializer(TeamInfo.class, new TeamInfoDeserializer(objectMapper));

        objectMapper.registerModule(module);
        return objectMapper;
    }

    public <T> T doGet(String url, Class<T> objectType) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.getType());

        HttpEntity<?> entity = new HttpEntity(headers);

        HttpEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        return objectMapper.readValue(response.getBody(), objectType);
    }
}
