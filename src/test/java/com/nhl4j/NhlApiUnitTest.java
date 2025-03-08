package com.nhl4j;

import com.nhl4j.domain.GameStatus;
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
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NhlApiUnitTest extends BaseApiUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NhlApi nhlApi;

    @Test
    public void getGameBoxscore_gameInPostGame_returnsGame() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/hockey/nhl/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("nhl/boxscore-postgame.json"));

        final var gameData = nhlApi.getBoxscore("401570064");

        assertNotNull(gameData);

        assertEquals(GameStatus.FINAL, gameData.getGameStatus());

        assertEquals("Edmonton Oilers", gameData.getHome().getFullName());
        assertEquals("Toronto Maple Leafs", gameData.getAway().getFullName());

        assertEquals("3", gameData.getHome().getStats().get(Stat.SCORE));
        assertEquals("4", gameData.getAway().getStats().get(Stat.SCORE));

        assertEquals(3, gameData.getHome().getLineScore().size());
        assertEquals(3, gameData.getAway().getLineScore().size());

        assertEquals("2", gameData.getHome().getLineScore().get(2));
        assertEquals("1", gameData.getAway().getLineScore().get(2));

        final var playerMcDavid = getPlayerFromTeam(gameData.getHome(), "3895074");
        assertEquals("2", playerMcDavid.getStats().get(Stat.HITS));
        assertEquals("9", playerMcDavid.getStats().get(Stat.SOGS));
        assertEquals("0", playerMcDavid.getStats().get(Stat.GOALS));
        assertEquals("5", playerMcDavid.getStats().get(Stat.FACEOFF_WINS));
        assertEquals("0", playerMcDavid.getStats().get(Stat.PIMS));
        assertEquals("0", playerMcDavid.getStats().get(Stat.ASSISTS));

        final var playerMatthews = getPlayerFromTeam(gameData.getAway(), "4024123");
        assertEquals("0", playerMatthews.getStats().get(Stat.HITS));
        assertEquals("7", playerMatthews.getStats().get(Stat.SOGS));
        assertEquals("0", playerMatthews.getStats().get(Stat.GOALS));
        assertEquals("11", playerMatthews.getStats().get(Stat.FACEOFF_WINS));
        assertEquals("0", playerMatthews.getStats().get(Stat.PIMS));
        assertEquals("2", playerMatthews.getStats().get(Stat.ASSISTS));

        assertEquals(7, gameData.getEvents().size());
    }

    @Test
    public void getGameBoxscore_gameInPreGame_returnsGame() throws StatsApiException {
        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/hockey/nhl/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("nhl/boxscore-pregame.json"));

        final var gameData = nhlApi.getBoxscore("401570064");

        assertNotNull(gameData);

        assertEquals(GameStatus.UPCOMING, gameData.getGameStatus());

        assertEquals(0, gameData.getEvents().size());
    }

    private Player getPlayerFromTeam(Team team, String playerId) {
        return team.getRoster().stream()
                .filter(it -> it.getId().equals(playerId))
                .findFirst().orElseThrow();
    }
}
