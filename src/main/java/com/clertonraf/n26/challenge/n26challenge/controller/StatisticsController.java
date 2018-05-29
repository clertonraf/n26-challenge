package com.clertonraf.n26.challenge.n26challenge.controller;

import com.clertonraf.n26.challenge.n26challenge.entity.Statistics;
import com.clertonraf.n26.challenge.n26challenge.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/statistics")
    public Statistics getStatistics() {
        return statisticsService.getStats();
    }

}
