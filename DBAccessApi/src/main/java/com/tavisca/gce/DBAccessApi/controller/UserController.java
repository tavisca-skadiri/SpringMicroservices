package com.tavisca.gce.DBAccessApi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tavisca.gce.DBAccessApi.model.Request;
import com.tavisca.gce.DBAccessApi.model.User;
import com.tavisca.gce.DBAccessApi.repository.RequestRepository;
import com.tavisca.gce.DBAccessApi.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserRepository repo;

    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody String json, HttpServletRequest request) throws IOException {
        String transactionId = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            transactionId = jsonObject.getString("transactionId");
            String jsonUserData = jsonObject.getJSONObject("userData").toString();
            User userData = new ObjectMapper().readValue(jsonUserData, User.class);

            if (repo.save(userData) != null)
                return insertUser(request, transactionId);
            return callExceptionApi(request, transactionId);
        } catch (Exception e) {
            insertRequest(transactionId, false, request.getRequestURI(), "/exceptionlogger");
            throw e;
        }
    }

    private ResponseEntity<String> insertUser(HttpServletRequest request, String transactionId) {
        insertRequest(transactionId, true, request.getRequestURI(), "--");
        return ResponseEntity.status(HttpStatus.OK).body("User added successfully");
    }

    private ResponseEntity<String> callExceptionApi(HttpServletRequest request, String transactionId) {
        insertRequest(transactionId, false, request.getRequestURI(), "/exceptionlogger");
        throw new RuntimeException("User DB insertion failed");
    }

    private void insertRequest(String transactionId, boolean isValid, String fromService, String toService) {
        Request r = new Request(transactionId, isValid, Instant.now().toString(), fromService, toService);
        requestRepository.save(r);
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        boolean isValid = repo.findAll() != null;
        insertRequest(UUID.randomUUID().toString(), isValid, request.getRequestURI(), "UiApi");
        return ResponseEntity.status(HttpStatus.OK).body(repo.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable("id") Integer id, HttpServletRequest request) {
        Optional<User> user = repo.findById(id);
        user.orElseThrow(() -> new RuntimeException("User not found"));
        insertRequest(UUID.randomUUID().toString(), true, request.getRequestURI(), "UiApi");
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}