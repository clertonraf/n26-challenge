package com.clertonraf.n26.challenge.n26challenge.service;

import com.clertonraf.n26.challenge.n26challenge.entity.Index;
import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;

import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentMap;

public interface TransactionDataService {

    void save(ZonedDateTime timestamp, Transaction transaction);

    ConcurrentMap<Index, Transaction> getTransactionsFromLastMinute();

    void clear();
}
