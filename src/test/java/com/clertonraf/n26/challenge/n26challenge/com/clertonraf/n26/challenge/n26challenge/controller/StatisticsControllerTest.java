package com.clertonraf.n26.challenge.n26challenge.com.clertonraf.n26.challenge.n26challenge.controller;

import com.clertonraf.n26.challenge.n26challenge.entity.Statistics;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.number.IsCloseTo.closeTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticsControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired private TransactionDataService transactionDataService;

    @Before
    public void setUp() {
        transactionDataService.clear();
    }

    @Test
    public void shouldGet_1_nothing() throws InterruptedException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Statistics> responseEntity = testRestTemplate.getForEntity("http://localhost:"+port+"/statistics",Statistics.class);
        Statistics statistics = responseEntity.getBody();

        assertThat(statistics.getAvg(),is(0.0));
        assertThat(statistics.getCount(),is(0L));
        assertThat(statistics.getMax(),is(0.0));
        assertThat(statistics.getMin(),is(0.0));
        assertThat(statistics.getSum(),is(0.0));

    }

    @Test
    public void shouldGet_2_forOne() throws InterruptedException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long now = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity =
                new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        ResponseEntity<Statistics> responseEntity = testRestTemplate.getForEntity("http://localhost:"+port+"/statistics",Statistics.class);
        Statistics statistics = responseEntity.getBody();

        assertThat(statistics.getAvg(),is(12.3));
        assertThat(statistics.getCount(),is(1L));
        assertThat(statistics.getMax(),is(12.3));
        assertThat(statistics.getMin(),is(12.3));
        assertThat(statistics.getSum(),is(12.3));
    }

    @Test
    public void shouldGet_3_forMore() throws InterruptedException {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long now = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity =
                new HttpEntity<>("{\"amount\": 16.1,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);

        HttpEntity<String> entity1 =
                new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response1 = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity1,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        ResponseEntity<Statistics> responseEntity = testRestTemplate.getForEntity("http://localhost:"+port+"/statistics",Statistics.class);
        Statistics statistics = responseEntity.getBody();

        assertThat(statistics.getAvg(),closeTo(14.2,0.00000000000001));
        assertThat(statistics.getCount(),is(2L));
        assertThat(statistics.getMax(),is(16.1));
        assertThat(statistics.getMin(),is(12.3));
        assertThat(statistics.getSum(),closeTo(28.4,0.00000000000001));
    }
}
