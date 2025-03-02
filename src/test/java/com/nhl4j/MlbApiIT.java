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

public class MlbApiIT {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private MlbApi mlbApi;

    @BeforeEach
    public void setup() {
        mlbApi = new MlbApi(new RestTemplate());
    }

    @Test
    public void validRequest_getTeams_notNullAndAllTeamsReturned() throws StatsApiException {
        final var teams = mlbApi.getTeams();

        assertNotNull(teams);
        assertEquals(teams.size(), 30);
    }

    @Test
    public void validRequest_getTeam2_teamDataWithRoster() throws StatsApiException {
        final var team = mlbApi.getTeam("2");

        assertNotNull(team);
        assertTrue(!team.getRoster().isEmpty());
    }

    @Test
    public void validDate_getSchedule_returnsScheduleWithGames() throws StatsApiException, ParseException {
        final var today = Date.from(DATE_FORMAT.parse("2025-02-22").toInstant());

        Schedule scheduleData = mlbApi.getScheduleForDate(today);

        assertNotNull(scheduleData);
        assertTrue(!scheduleData.getGames().isEmpty());

        final var game = scheduleData.getGames().get(0);
        assertNotNull(game.getAway().getFullName());
        assertNotNull(game.getHome().getFullName());
        assertNotNull(game.getGameDate());

        final var gameDate = DATE_FORMAT.parse(game.getGameDate());
        assertEquals(today, gameDate);
    }

    @Test
    public void validGameId_getGameBoxscore_returnsGame() throws StatsApiException {
        final var gameData = mlbApi.getBoxscore("401570064");

        assertNotNull(gameData);
    }
}
