package com.nhl4j;

import com.nhl4j.domain.Schedule;
import com.nhl4j.exception.StatsApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NhlApiV1IT {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private NhlApiV1 nhlApi;

    @BeforeEach
    public void setup() {
        nhlApi = new NhlApiV1(new RestTemplate());
    }

    @Test
    public void validRequest_getTeams_notNullAndAllTeamsReturned() throws StatsApiException {
        final var teams = nhlApi.getTeams();

        assertNotNull(teams);
        assertEquals(teams.size(), 32);
    }

    @Test
    public void validRequest_getTeam3_teamDataWithRoster() throws StatsApiException {
        final var team = nhlApi.getTeam("3");

        assertNotNull(team);
    }

    @Test
    public void validDate_getSchedule_returnsScheduleWithGames() throws StatsApiException, ParseException {
        final var today = Date.from(DATE_FORMAT.parse("2022-11-25").toInstant());

        Schedule scheduleData = nhlApi.getScheduleForDate(today);

        assertNotNull(scheduleData);
        assertTrue(scheduleData.getGames().size() > 0);

        final var game = scheduleData.getGames().get(0);
        assertNotNull(game.getAway().getFullName());
        assertNotNull(game.getHome().getFullName());
        assertNotNull(game.getGameDate());

        final var gameDate = DATE_FORMAT.parse(game.getGameDate());
        assertEquals(today, gameDate);
    }

    @Test
    public void validGameId_getGameBoxscore_returnsGame() throws StatsApiException {
        final var gameData = nhlApi.getBoxscore("2022020767");

        assertNotNull(gameData);
    }
}
