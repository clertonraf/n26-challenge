package com.clertonraf.n26.challenge.n26challenge.com.clertonraf.n26.challenge.n26challenge.service;

import com.clertonraf.n26.challenge.n26challenge.entity.Index;
import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionDataServiceTest {

    @Autowired
    private TransactionDataService transactionDataService;

    @Before
    public void setUp() {
        transactionDataService.clear();
    }

    @Test
    public void transactionData_1_getEmpty() {
        ConcurrentMap<Index,Transaction> map = transactionDataService.getTransactionsFromLastMinute();
        assertTrue(map.isEmpty());
    }

    @Test
    public void transactionData_2_getTransactionsOnlyFromLastMinute_empty() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(61L);
        Transaction transaction = new Transaction();
        transaction.setTimestamp(now.toEpochSecond()*1000);
        transaction.setAmount(1.0);

        transactionDataService.save(now,transaction);
        ConcurrentMap<Index,Transaction> map = transactionDataService.getTransactionsFromLastMinute();
        assertTrue(map.isEmpty());
    }

    @Test
    public void transactionData_3_getTransactionFromLastMinute_ok() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Transaction transaction = new Transaction();
        transaction.setTimestamp(now.toEpochSecond()*1000);
        transaction.setAmount(3.0);

        transactionDataService.save(now,transaction);
        ConcurrentMap<Index,Transaction> map = transactionDataService.getTransactionsFromLastMinute();
        assertEquals(map.size(),1);

        map.values().stream().forEach(x->assertEquals(x.getAmount(),3.0,0.00001));
    }

    @Test
    public void transactionData_4_getTransactionFromLastMinute_alsoWithInvalidTimestamp_ok() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Transaction transaction = new Transaction();
        transaction.setTimestamp(now.toEpochSecond()*1000);
        transaction.setAmount(3.0);

        transactionDataService.save(now,transaction);

        Transaction transaction0 = new Transaction();
        transaction0.setTimestamp(now.toEpochSecond()*1000);
        transaction0.setAmount(5.0);

        Transaction transaction1 = new Transaction();
        transaction1.setTimestamp(now.minusSeconds(61L).toEpochSecond()*1000);
        transaction1.setAmount(7.0);

        transactionDataService.save(now,transaction0);

        transactionDataService.save(now.minusSeconds(61L),transaction1);
        ConcurrentMap<Index,Transaction> map = transactionDataService.getTransactionsFromLastMinute();
        assertEquals(map.size(),2);
        assertEquals(map.values().stream().mapToDouble(Transaction::getAmount).sum(),8.0,0.0001);
        assertEquals(map.values().stream().mapToDouble(Transaction::getAmount).max().orElse(0.0),5.0, 0.0001);
        assertEquals(map.values().stream().mapToDouble(Transaction::getAmount).min().orElse(0.0),3.0, 0.0001);
    }

    @Test( expected = InvalidTransactionTimeException.class)
    public void transactionData_5_getTransactionFromLastMinute_insertNullIndex() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Transaction transaction = new Transaction();
        transaction.setTimestamp(now.toEpochSecond()*1000);
        transaction.setAmount(5.0);

        transactionDataService.save(null,transaction);
    }
}
