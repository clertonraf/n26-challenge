package com.clertonraf.n26.challenge.n26challenge.controller;


import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionAmountException;
import com.clertonraf.n26.challenge.n26challenge.exceptions.InvalidTransactionTimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebRestControllerAdvice {

    @ExceptionHandler(InvalidTransactionTimeException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> invalidTime(InvalidTransactionTimeException exception) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(InvalidTransactionAmountException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> invalidAmount(InvalidTransactionAmountException exception) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
