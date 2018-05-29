package com.clertonraf.n26.challenge.n26challenge.controller;

import com.clertonraf.n26.challenge.n26challenge.entity.Transaction;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import com.clertonraf.n26.challenge.n26challenge.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody Transaction transaction) {
        service.register(transaction);
    }

}
