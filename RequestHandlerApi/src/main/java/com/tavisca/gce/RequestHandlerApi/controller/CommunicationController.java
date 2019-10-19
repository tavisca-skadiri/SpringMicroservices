package com.tavisca.gce.RequestHandlerApi.controller;

import com.tavisca.gce.RequestHandlerApi.model.Request;
import com.tavisca.gce.RequestHandlerApi.repository.RequestRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/gateway")
    public ResponseEntity<String> requestHandler(@RequestBody String json, HttpServletRequest request) {
        String transactionId = UUID.randomUUID().toString();
        try {
            if (!json.contains("userData"))
                throw new RuntimeException("Invalid user data");
            return callValidatorApi(request, transactionId, json);
        } catch (Exception e) {
            insertRequest(transactionId, false, request.getRequestURI(), "/exceptionlogger");
            throw e;
        }
    }

    private ResponseEntity<String> callValidatorApi(HttpServletRequest request, String transactionId, String json) {
        JSONObject jsonRequest = new JSONObject(json);
        jsonRequest.put("transactionId", transactionId);
        final URI uri = URI.create("http://localhost:8080/validator");
        insertRequest(transactionId, true, request.getRequestURI(), uri.getPath());
        return new RestTemplate().postForEntity(uri, jsonRequest.toString(), String.class);
    }

    private void insertRequest(String transactionId, boolean isValid, String fromService, String toService) {
        Request r = new Request(transactionId, isValid, Instant.now().toString(), fromService, toService);
        requestRepository.save(r);
    }
}