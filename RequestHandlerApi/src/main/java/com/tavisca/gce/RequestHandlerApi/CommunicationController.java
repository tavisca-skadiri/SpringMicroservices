package com.tavisca.gce.RequestHandlerApi;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@RestController
public class CommunicationController {
    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/send")
    public ResponseEntity<String> sendToValidatorApi(@RequestBody String json, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject(json);
        if(json.contains("userData")){
            String transactionId = UUID.randomUUID().toString();
            jsonObject.put("transactionId", transactionId);
            final URI uri = URI.create("http://localhost:8080/validate");
            insertRequest(transactionId, true, request.getRequestURI(), uri.getPath());
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.postForEntity(uri, jsonObject.toString(), String.class);
        }
        throw new RuntimeException("Empty user data");
    }

    @GetMapping("/error")
    public ResponseEntity<String> handleError(Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }

    private void insertRequest(String transactionId, boolean isValid, String fromService, String toService) {
        requestRepository.save(
                new Request(
                        transactionId, isValid,
                        Instant.now().toString(),
                        fromService, toService));
    }
}