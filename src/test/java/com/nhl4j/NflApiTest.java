package com.nhl4j;

import com.nhl4j.domain.schedule.Schedule;
import com.nhl4j.exception.StatsApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class NflApiTest {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private NflApi nflApi;

    @BeforeEach
    public void setup() {
        nflApi = new NflApi(new RestTemplate());
    }

    @Test
    @Disabled
    public void validRequest_getTeams_notNullAndAllTeamsReturned() throws StatsApiException {
        final var teams = nflApi.getTeams();

        assertNotNull(teams);
        assertEquals(teams.getTeams().size(), 32);
    }

    @Test
    @Disabled
    public void validRequest_getTeam3_teamDataWithRoster() throws StatsApiException {
        final var team = nflApi.getTeam("3").getTeams().get(0);

        assertNotNull(team);
    }

    @Test
    public void validDate_getSchedule_returnsScheduleWithGames() throws StatsApiException, ParseException {
        final var today = Date.from(DATE_FORMAT.parse("2023-01-21").toInstant());

        Schedule scheduleData = nflApi.getScheduleForDate(today);

        assertNotNull(scheduleData);
        assertTrue(scheduleData.getGames().size() > 0);

        final var game = scheduleData.getGames().get(0);
        assertNotNull(game.getAway().getName());
        assertNotNull(game.getHome().getName());
        assertNotNull(game.getGameDate());

        final var gameDate = DATE_FORMAT.parse(game.getGameDate());
        assertEquals(today, gameDate);
    }

    @Test
    @Disabled
    public void validGameId_getGameBoxscore_returnsGame() throws StatsApiException {
        final var gameData = nflApi.getGameBoxscore("2022020357");

        assertNotNull(gameData);
    }
}
