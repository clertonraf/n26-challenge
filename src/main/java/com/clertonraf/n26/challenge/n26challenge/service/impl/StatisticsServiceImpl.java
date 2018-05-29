package com.clertonraf.n26.challenge.n26challenge.service.impl;

import com.clertonraf.n26.challenge.n26challenge.entity.Index;
import com.clertonraf.n26.challenge.n26challenge.entity.Statistics;
import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.service.StatisticsService;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    TransactionDataService transactionDataService;

    public Statistics getStats() {
        ConcurrentMap<Index, Transaction> map =
                transactionDataService.getTransactionsFromLastMinute();

        Statistics statistics = new Statistics();
        statistics.setAvg(
                map.values().stream().mapToDouble(Transaction::getAmount).average()
                        .orElse(0.0)
        );
        statistics.setCount(
                map.values().stream().mapToDouble(Transaction::getAmount).count()
        );
        statistics.setMax(
                map.values().stream().mapToDouble(Transaction::getAmount).max()
                        .orElse(0.0)
        );
        statistics.setMin(
                map.values().stream().mapToDouble(Transaction::getAmount).min()
                        .orElse(0.0)
        );
        statistics.setSum(
                map.values().stream().mapToDouble(Transaction::getAmount).sum()
        );

        return statistics;
    }

}
