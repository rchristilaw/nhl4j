package com.nhl4j;

import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;

import static com.nhl4j.domain.GameStatus.UPCOMING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MlbApiUnitTest extends BaseApiUnitTest{

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MlbApi mlbApi;

    @Test
    public void getGameBoxscore_gameInPostGame_returnsGame() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/baseball/mlb/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("mlb/boxscore-postgame.json"));

        final var gameData = mlbApi.getBoxscore("401570064");

        assertNotNull(gameData);

        assertEquals(GameStatus.FINAL, gameData.getGameStatus());

        assertEquals("Toronto Blue Jays", gameData.getHome().getFullName());
        assertEquals("Texas Rangers", gameData.getAway().getFullName());

        assertEquals("7", gameData.getHome().getStats().get(Stat.SCORE));
        assertEquals("3", gameData.getAway().getStats().get(Stat.SCORE));

        assertEquals(8, gameData.getHome().getLineScore().size());
        assertEquals(9, gameData.getAway().getLineScore().size());

        assertEquals("3", gameData.getHome().getLineScore().get(2));
        assertEquals("1", gameData.getAway().getLineScore().get(5));

        assertEquals("9", gameData.getHome().getStats().get(Stat.MLB_HITS));
        assertEquals("1", gameData.getHome().getStats().get(Stat.MLB_ERRORS));

        assertEquals("10", gameData.getAway().getStats().get(Stat.MLB_HITS));
        assertEquals("0", gameData.getAway().getStats().get(Stat.MLB_ERRORS));

        final var playerGuerrero = getPlayerFromTeam(gameData.getHome(), "35002");
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_HITS));
        assertEquals("0", playerGuerrero.getStats().get(Stat.MLB_BB));
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_RUNS));
        assertEquals("0", playerGuerrero.getStats().get(Stat.MLB_K));
        assertEquals("1", playerGuerrero.getStats().get(Stat.MLB_HR));
        assertEquals("3", playerGuerrero.getStats().get(Stat.MLB_AB));
        assertEquals("2", playerGuerrero.getStats().get(Stat.MLB_RBI));

        final var playerBerrios = getPlayerFromTeam(gameData.getHome(), "32811");
        assertEquals("7.0", playerBerrios.getStats().get(Stat.MLB_PITCHER_IP));
        assertEquals("5", playerBerrios.getStats().get(Stat.MLB_PITCHER_K));
        assertEquals("1", playerBerrios.getStats().get(Stat.MLB_PITCHER_RUNS));
        assertEquals("0", playerBerrios.getStats().get(Stat.MLB_PITCHER_HR));
        assertEquals("6", playerBerrios.getStats().get(Stat.MLB_PITCHER_HITS));
        assertEquals("0", playerBerrios.getStats().get(Stat.MLB_PITCHER_BB));
        assertEquals("1", playerBerrios.getStats().get(Stat.MLB_PITCHER_EARNED_RUNS));
    }

    @Test
    public void getGameBoxscore_gameInPreGame_returnsGame() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/baseball/mlb/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("mlb/boxscore-pregame.json"));

        final var gameData = mlbApi.getBoxscore("401570064");

        assertNotNull(gameData);

        assertEquals(GameStatus.UPCOMING, gameData.getGameStatus());

        assertEquals(0, gameData.getEvents().size());
    }

    @Test
    public void getSchedule_dayOfSchedule_returnsScheduleWithBettingLines() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/baseball/mlb/scoreboard?dates=20250223"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("mlb/schedule.json"));

        final var schedule = mlbApi.getScheduleForDate(Date.valueOf("2025-02-23"));

        assertNotNull(schedule);

        assertEquals(16, schedule.getGames().size());

        final var jaysVsRedSoxGame = getGameById(schedule, "401704175");
        assertEquals(UPCOMING, jaysVsRedSoxGame.getGameStatus());

        assertEquals("Blue Jays", jaysVsRedSoxGame.getAway().getNickName());
        assertEquals("Red Sox", jaysVsRedSoxGame.getHome().getNickName());

        assertEquals(-1.5f, jaysVsRedSoxGame.getBettingLine().getSpread());
        assertEquals(7.5f, jaysVsRedSoxGame.getBettingLine().getTotalPoints());

        // 1:05 PM EST
        assertEquals("2025-02-23T18:05Z", jaysVsRedSoxGame.getGameDate());
    }

    private Game getGameById(Schedule schedule, String gameId) {
        return schedule.getGames().stream()
                .filter(it -> it.getId().equals(gameId))
                .findFirst().orElseThrow();
    }

    private Player getPlayerFromTeam(Team team, String playerId) {
        return team.getRoster().stream()
                .filter(it -> it.getId().equals(playerId))
                .findFirst().orElseThrow();
    }
}
