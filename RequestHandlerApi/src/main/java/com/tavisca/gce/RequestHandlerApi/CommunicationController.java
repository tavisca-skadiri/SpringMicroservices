package com.tavisca.gce.RequestHandlerApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@RestController
public class CommunicationController {
    @PostMapping("/send")
    public ResponseEntity sendToValidatorApi(@RequestBody User userData) {
        final String uri = "http://localhost:8080/validate";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(uri, userData, User.class);
        //if (responseEntity.getStatusCode().isError())
            throw new RuntimeException();
        //return ResponseEntity.status(HttpStatus.OK).body("User added successfully");
    }

    @GetMapping("/error")
    public ResponseEntity<String> handleError(Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }

}