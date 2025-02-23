package com.nhl4j;

import com.nhl4j.domain.Player;
import com.nhl4j.domain.Stat;
import com.nhl4j.domain.Team;
import com.nhl4j.exception.StatsApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MlbApiUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MlbApi mlbApi;


    @Test
    public void getGameBoxscore_getGameBoxscore_returnsGame() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/baseball/mlb/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("mlb/boxscore-postgame.json"));

        final var gameData = mlbApi.getGameDetails("401570064");

        assertNotNull(gameData);

        assertEquals("Toronto Blue Jays", gameData.getHome().getFullName());
        assertEquals("Texas Rangers", gameData.getAway().getFullName());

        assertEquals("7", gameData.getHome().getStats().get(Stat.SCORE));
        assertEquals("3", gameData.getAway().getStats().get(Stat.SCORE));

        assertEquals(8, gameData.getHome().getLineScore().size());
        assertEquals(9, gameData.getAway().getLineScore().size());

        assertEquals("3", gameData.getHome().getLineScore().get(2));
        assertEquals("1", gameData.getAway().getLineScore().get(5));

        final var playerGuerrero = getPlayerFromTeam(gameData.getHome(), "35002");
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_HITS));
        assertEquals("0", playerGuerrero.getStats().get(Stat.MLB_BB));
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_RUNS));
        assertEquals("0", playerGuerrero.getStats().get(Stat.MLB_K));
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_HR));
        assertEquals("3", playerGuerrero.getStats().get(Stat.MLB_AB));
        assertEquals("2", playerGuerrero.getStats().get(Stat.MLB_RBI));

        final var playerBerrios = getPlayerFromTeam(gameData.getHome(), "32811");
        assertEquals("5", playerBerrios.getStats().get(Stat.MLB_PITCHER_K));
        assertEquals("1", playerBerrios.getStats().get(Stat.MLB_PITCHER_RUNS));
        assertEquals("0", playerBerrios.getStats().get(Stat.MLB_PITCHER_HR));
        assertEquals("6", playerBerrios.getStats().get(Stat.MLB_PITCHER_HITS));
        assertEquals("0", playerBerrios.getStats().get(Stat.MLB_PITCHER_BB));
    }

    private Player getPlayerFromTeam(Team team, String playerId) {
        return team.getRoster().stream()
                .filter(it -> it.getId().equals(playerId))
                .findFirst().orElseThrow();
    }

    private ResponseEntity<String> mockResponse(String mockResponseFilePath) {
        return ResponseEntity.ok(getFromFile(mockResponseFilePath));
    }

    private String getFromFile(String fileName) {
        try {
            return new String(Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream(fileName)).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
