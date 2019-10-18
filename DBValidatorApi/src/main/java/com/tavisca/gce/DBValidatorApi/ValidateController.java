package com.tavisca.gce.DBValidatorApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@RestController
public class ValidateController {
    @Autowired
    private ValidateService validateService;

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/validate")
    public ResponseEntity<String> addUser(@RequestBody String json, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject(json);
        String transactionId = jsonObject.getString("transactionId");
        ObjectMapper mapper = new ObjectMapper();
        try {
            User userData = mapper.readValue(jsonObject.getJSONObject("userData").toString(), User.class);
            if (validateService.isValid(userData)) {
                final URI uri = URI.create("http://localhost:7070/user");
                insertRequest(transactionId, true, request.getRequestURI(), uri.getPath());
                RestTemplate restTemplate = new RestTemplate();
                return restTemplate.postForEntity(uri, json, String.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Invalid user details");
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