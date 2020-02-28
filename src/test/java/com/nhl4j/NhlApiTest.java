package com.nhl4j;

import com.nhl4j.domain.TeamsData;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NhlApiTest {

    private NhlApi nhlApi;

    @BeforeMethod
    public void setup() {
        nhlApi = new NhlApi(new RestTemplate());
    }

    @Test
    public void getTeams() {
        TeamsData teamData = nhlApi.getTeams();

        Assert.assertNotNull(teamData);
        Assert.assertEquals(teamData.getTeams().size(), 31);
    }
}
