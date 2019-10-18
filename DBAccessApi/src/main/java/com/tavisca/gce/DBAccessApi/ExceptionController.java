package com.tavisca.gce.DBAccessApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@ControllerAdvice
public class ExceptionController {
    @Autowired
    private RequestRepository requestRepository;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(final Exception e, HttpServletRequest request) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        insertRequest(UUID.randomUUID().toString(), request.getRequestURI(), uri.getPath());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }

    private void insertRequest(String transactionId, String fromService, String toService) {
        requestRepository.save(
                new Request(
                        transactionId, false,
                        Instant.now().toString(),
                        fromService, toService));
    }
}