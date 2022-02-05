package com.grijesh.playground.controller.advice;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Controller Advice to handle the exception
 *
 * @author Grijesh Saini
 */
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleClientException(WebClientResponseException webClientResponseException) {
        return ResponseEntity.status(webClientResponseException.getStatusCode())
                .body(webClientResponseException.getResponseBodyAsString());
    }

}
