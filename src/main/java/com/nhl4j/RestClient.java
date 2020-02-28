package com.nhl4j;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhl4j.domain.TeamsData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
