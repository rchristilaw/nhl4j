package com.nhl4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nhl4j.domain.*;
import com.nhl4j.serializers.espn.*;
import com.nhl4j.serializers.nhl.NhlGameDeserializer;
import com.nhl4j.serializers.nhl.NhlScheduleDeserializer;
import com.nhl4j.serializers.nhl.NhlTeamDeserializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class BaseApi {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BaseApi(RestTemplate restTemplate, ApiSource apiSource) {
        this.restTemplate = restTemplate;
        this.objectMapper = configureObjectMapper(apiSource);
    }

    private ObjectMapper configureObjectMapper(ApiSource apiSource) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule module = new SimpleModule();

        if (apiSource.isEspn()) {
            module.addDeserializer(Schedule.class, new EspnScheduleDeserializer());
            module.addDeserializer(Game.class, new EspnGameDeserializer(apiSource));
            module.addDeserializer(Team.class, new EspnTeamDeserializer());
            module.addDeserializer(Player[].class, new EspnRosterDeserializer());
            module.addDeserializer(Team[].class, new EspnTeamsListDeserializer(objectMapper));
        } else {
            module.addDeserializer(Game.class, new NhlGameDeserializer());
            module.addDeserializer(Schedule.class, new NhlScheduleDeserializer(objectMapper));
            module.addDeserializer(Team[].class, new NhlTeamDeserializer());
        }

        objectMapper.registerModule(module);
        return objectMapper;
    }

    public <T> T doGet(String url, Class<T> objectType) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON.getType());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        return objectMapper.readValue(response.getBody(), objectType);
    }
}
