package com.clertonraf.n26.challenge.n26challenge.service.impl;

import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionAmountException;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionDataService dataService;

    @Override
    public void register(Transaction transaction) {
        if(transaction.getAmount() <= 0.0) {
            throw new InvalidTransactionAmountException();
        }

        Instant instant = Instant.ofEpochMilli(transaction.getTimestamp());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);

        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(60L);
        if(zonedDateTime.isBefore(now)) {
            throw new InvalidTransactionTimeException();
        }

        dataService.save(zonedDateTime, transaction);
    }
}
