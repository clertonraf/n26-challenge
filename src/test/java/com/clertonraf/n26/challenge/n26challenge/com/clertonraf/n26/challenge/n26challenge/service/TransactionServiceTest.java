package com.clertonraf.n26.challenge.n26challenge.com.clertonraf.n26.challenge.n26challenge.service;

import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionAmountException;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired private TransactionDataService transactionDataService;

    @Before
    public void setUp() {
        transactionDataService.clear();
    }

    @Test
    public void registerOk() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0.000001);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);

        transactionService.register(transaction);
    }

    @Test
    public void registerOk_twoConcurrentTransactions() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0.000001);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);

        transactionService.register(transaction);

        Transaction transaction1 = new Transaction();
        transaction1.setAmount(0.000001);
        transaction1.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);

        transactionService.register(transaction1);
    }

    @Test(expected = InvalidTransactionAmountException.class)
    public void registerWrongAmount_zero() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0.00);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);

        transactionService.register(transaction);
    }

    @Test(expected = InvalidTransactionAmountException.class)
    public void registerWrongAmount_negative() {
        Transaction transaction = new Transaction();
        transaction.setAmount(-0.01);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond() * 1000);

        transactionService.register(transaction);
    }

    @Test(expected = InvalidTransactionTimeException.class)
    public void registerWrongTimestamp_expired() {
        Transaction transaction = new Transaction();
        transaction.setAmount(0.01);
        transaction.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(61L).toEpochSecond() * 1000);

        transactionService.register(transaction);
    }

}
