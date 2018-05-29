package com.clertonraf.n26.challenge.n26challenge.com.clertonraf.n26.challenge.n26challenge.controller;

import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment  = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

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
    public void shouldCreate_now() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long now = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity =
                new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldCreate_concurrentTimestamp() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long now = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity =
                new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));

        HttpEntity<String> entity2 =
                new HttpEntity<>("{\"amount\": 13.3,\"timestamp\": "+ now +"}", headers);
        ResponseEntity<String> response2 = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity2,String.class);
        assertThat(response2.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldCreate_almostNow() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long almostNow = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(1L).toEpochSecond() * 1000;
        HttpEntity<String> entity =
                new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ almostNow +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void shouldNotCreate_pastTimestamp() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": 1478192204000}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldNotCreate_nearPastTimestamp() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        long nearPast = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(61L).toEpochSecond() * 1000;
        HttpEntity<String> entity = new HttpEntity<>("{\"amount\": 12.3,\"timestamp\": "+ nearPast +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldNotCreate_wrongAmount() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        long nearPast = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity = new HttpEntity<>("{\"amount\": -1.0,\"timestamp\": "+ nearPast +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldNotCreate_noAmount() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        long nearPast = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000;
        HttpEntity<String> entity = new HttpEntity<>("{\"timestamp\": "+ nearPast +"}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldNotCreate_noTimestamp() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{\"amount\": 1.0}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void shouldNotCreate_nothing() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("{}", headers);
        ResponseEntity<String> response = testRestTemplate.
                postForEntity("http://localhost:" + port+"/transactions", entity,String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

}
