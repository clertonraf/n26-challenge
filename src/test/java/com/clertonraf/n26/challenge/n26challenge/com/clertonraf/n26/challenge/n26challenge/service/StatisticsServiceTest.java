package com.clertonraf.n26.challenge.n26challenge.com.clertonraf.n26.challenge.n26challenge.service;

import com.clertonraf.n26.challenge.n26challenge.entity.Statistics;
import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.service.StatisticsService;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticsServiceTest {

    @Autowired
    private TransactionDataService transactionDataService;

    @Before
    public void setUp() {
        transactionDataService.clear();
    }

    @Autowired
    private StatisticsService statisticsService;

    @Autowired TransactionService transactionService;

    @Test
    public void shouldGet_1_nothing() {
        Statistics statistics = statisticsService.getStats();
        assertEquals(statistics.getSum(),0.0,0.00001);
        assertEquals(statistics.getAvg(),0.0,0.00001);
        assertEquals(statistics.getMax(),0.0,0.00001);
        assertEquals(statistics.getMin(),0.0,0.00001);
        assertEquals(statistics.getCount(),0L);
    }

    @Test
    public void shouldGet_2_simpleResult() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1.0);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);
        transactionService.register(transaction);

        Statistics statistics = statisticsService.getStats();
        assertEquals(statistics.getSum(),1.0,0.00001);
        assertEquals(statistics.getAvg(),1.0,0.00001);
        assertEquals(statistics.getMax(),1.0,0.00001);
        assertEquals(statistics.getMin(),1.0,0.00001);
        assertEquals(statistics.getCount(),1L);
    }

    @Test
    public void shouldGet_3_complexResult() {
        Transaction transaction = new Transaction();
        transaction.setAmount(1.0);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);
        transactionService.register(transaction);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(3.0);
        transaction1.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);
        transactionService.register(transaction1);

        Statistics statistics = statisticsService.getStats();
        assertEquals(statistics.getSum(),4.0,0.00001);
        assertEquals(statistics.getAvg(),2.0,0.00001);
        assertEquals(statistics.getMax(),3.0,0.00001);
        assertEquals(statistics.getMin(),1.0,0.00001);
        assertEquals(statistics.getCount(),2L);
    }
}
