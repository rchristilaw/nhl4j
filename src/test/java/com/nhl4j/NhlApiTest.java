package com.nhl4j;

import com.nhl4j.domain.Boxscore;
import com.nhl4j.domain.schedule.Schedule;
import com.nhl4j.exception.NhlApiException;
import lombok.val;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class NhlApiTest {

    private NhlApi nhlApi;

    @BeforeMethod
    public void setup() {
        nhlApi = new NhlApi(new RestTemplate());
    }

    @Test
    public void validRequest_getTeams_notNullAndAllTeamsReturned() throws NhlApiException {
        val teams = nhlApi.getTeams();

        Assert.assertNotNull(teams);
        Assert.assertEquals(teams.getTeams().size(), 31);
    }

    @Test
    public void validDate_getSchedule_returnsScheduleWithGames () throws NhlApiException, ParseException {
        Calendar cal = Calendar.getInstance();
        cal.set(2021,2,13);
        val today = cal.getTime();

        Schedule scheduleData = nhlApi.getScheduleForDate(today);

        Assert.assertNotNull(scheduleData);
        Assert.assertTrue(scheduleData.getGames().size() > 0);
        val game = scheduleData.getGames().get(0);
        Assert.assertNotNull(game.getAway().getName());
        Assert.assertNotNull(game.getHome().getName());
        Assert.assertNotNull(game.getGameDate());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");;
        val gameDate = df.parse(game.getGameDate());

        Assert.assertEquals(getLocalDateFromDate(today),
                getLocalDateFromDate(gameDate));
    }

    @Test
    public void validGameId_getGameBoxscore_returnsGame() throws NhlApiException {
        Boxscore gameData = nhlApi.getGameBoxscore(2020020433);

        Assert.assertNotNull(gameData);
    }

    private LocalDate getLocalDateFromDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
