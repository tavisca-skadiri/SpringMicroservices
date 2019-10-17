package com.tavisca.gce.DBValidatorApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class ValidateController {
    @Autowired
    private UserRepository repo;

    @PostMapping("/validate")
    public ResponseEntity addUser(@RequestBody User userData) {

        return ResponseEntity.status(HttpStatus.OK).body(repo.save(userData));
    }

    @GetMapping("/error")
    public ResponseEntity<String> handleError(Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }
}