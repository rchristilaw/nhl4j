package com.nhl4j;

import com.nhl4j.domain.*;
import com.nhl4j.exception.StatsApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NflApiUnitTest extends BaseApiUnitTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NflApi nflApi;

    @Test
    public void getGameBoxscore_gameInPostGame_returnsGame() throws StatsApiException {

        when(restTemplate.exchange(
                eq("https://site.api.espn.com/apis/site/v2/sports/football/nfl/summary?event=401570064"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class))
        ).thenReturn(mockResponse("nfl/boxscore-postgame.json"));

        final var gameData = nflApi.getGameDetails("401570064");

        assertNotNull(gameData);

        assertEquals(GameStatus.FINAL, gameData.getGameStatus());

        assertEquals("San Francisco 49ers", gameData.getHome().getFullName());
        assertEquals("Dallas Cowboys", gameData.getAway().getFullName());

        assertEquals("30", gameData.getHome().getStats().get(Stat.SCORE));
        assertEquals("24", gameData.getAway().getStats().get(Stat.SCORE));

        assertEquals(4, gameData.getHome().getLineScore().size());
        assertEquals(4, gameData.getAway().getLineScore().size());

        assertEquals("3", gameData.getHome().getLineScore().get(3));
        assertEquals("14", gameData.getAway().getLineScore().get(3));

        final var playerPurdy = getPlayerFromTeam(gameData.getHome(), "4361741");
        assertEquals("56", playerPurdy.getStats().get(Stat.RUSHING_YARDS));
        assertEquals("18", playerPurdy.getStats().get(Stat.PASSING_COMPLETIONS));
        assertEquals("1", playerPurdy.getStats().get(Stat.RUSHING_TDS));
        assertEquals("260", playerPurdy.getStats().get(Stat.PASSING_YARDS));
        assertEquals("8", playerPurdy.getStats().get(Stat.RUSHING_ATTEMPTS));
        assertEquals("1", playerPurdy.getStats().get(Stat.PASSING_TDS));
        assertEquals("16", playerPurdy.getStats().get(Stat.RUSHING_LONG));
        assertEquals("0", playerPurdy.getStats().get(Stat.PASSING_INTS));

        final var playerLamb = getPlayerFromTeam(gameData.getAway(), "4241389");
        assertEquals("2", playerLamb.getStats().get(Stat.RECEIVING_TDS));
        assertEquals("13", playerLamb.getStats().get(Stat.RECEPTIONS));
        assertEquals("146", playerLamb.getStats().get(Stat.RECEIVING_YARDS));
        assertEquals("29", playerLamb.getStats().get(Stat.RECEIVING_LONG));

        assertEquals(10, gameData.getEvents().size());
    }

    private Player getPlayerFromTeam(Team team, String playerId) {
        return team.getRoster().stream()
                .filter(it -> it.getId().equals(playerId))
                .findFirst().orElseThrow();
    }
}
