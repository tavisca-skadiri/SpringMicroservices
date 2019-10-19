package com.tavisca.gce.DBValidatorApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavisca.gce.DBValidatorApi.model.Request;
import com.tavisca.gce.DBValidatorApi.model.User;
import com.tavisca.gce.DBValidatorApi.repository.RequestRepository;
import com.tavisca.gce.DBValidatorApi.service.ValidateService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;

@RestController
public class ValidateController {
    @Autowired
    private ValidateService validateService;

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/validator")
    public ResponseEntity<String> addUser(@RequestBody String json, HttpServletRequest request) throws IOException {
        String transactionId = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            transactionId = jsonObject.getString("transactionId");
            String jsonUserData = jsonObject.getJSONObject("userData").toString();
            User userData = new ObjectMapper().readValue(jsonUserData, User.class);

            if (validateService.isValid(userData))
                return callAccessApi(request, transactionId, json);
            throw new RuntimeException("Invalid user details");
        } catch (Exception e) {
            insertRequest(transactionId, false, request.getRequestURI(), "/exceptionlogger");
            throw e;
        }
    }

    private ResponseEntity<String> callAccessApi(HttpServletRequest request, String transactionId, String json) {
        final URI uri = URI.create("http://localhost:7070/user");
        insertRequest(transactionId, true, request.getRequestURI(), uri.getPath());
        return new RestTemplate().postForEntity(uri, json, String.class);
    }

    private void insertRequest(String transactionId, boolean isValid, String fromService, String toService) {
        Request r = new Request(transactionId, isValid, Instant.now().toString(), fromService, toService);
        requestRepository.save(r);
    }
}