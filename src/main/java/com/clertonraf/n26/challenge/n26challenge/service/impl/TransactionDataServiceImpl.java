package com.clertonraf.n26.challenge.n26challenge.service.impl;

import com.clertonraf.n26.challenge.n26challenge.entity.Index;
import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class TransactionDataServiceImpl implements TransactionDataService {

    private static final ConcurrentMap<Index, Transaction> transactions
            = new ConcurrentHashMap<>();


    public void save(ZonedDateTime timestamp, Transaction transaction) {
        if(timestamp == null || transaction == null) {
            throw new InvalidTransactionTimeException();
        }
        transactions.put(new Index(timestamp),transaction);
    }

    public void clear() {
        transactions.clear();
    }

    public ConcurrentMap<Index, Transaction> getTransactionsFromLastMinute() {
        return transactions.entrySet()
                .stream()
                .filter(e -> e.getKey()
                                .getZonedDateTime()
                                .isAfter(
                                        ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(1)
                                )
                )
                .collect(
                        Collectors.toConcurrentMap(
                                e -> e.getKey(), e -> e.getValue()
                        )
                );
    }
}