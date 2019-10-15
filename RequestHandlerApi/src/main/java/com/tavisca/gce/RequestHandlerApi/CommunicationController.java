package com.tavisca.gce.RequestHandlerApi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CommunicationController {
    @PostMapping("/send")
    public ResponseEntity sendToValidatorApi(@RequestBody User userData) {
        final String uri = "http://localhost:8080/validate";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<User> responseEntity = restTemplate.postForEntity(uri, userData, User.class);
        if(responseEntity.getStatusCode().isError())
            return responseEntity;
        return ResponseEntity.status(HttpStatus.OK).body("User added successfully");
    }
}