package com.tavisca.gce.DBValidatorApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger");
        return new RestTemplate().postForEntity(uri, e, String.class);
    }
}