package com.tavisca.gce.DBAccessApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserRepository repo;

    @Autowired
    private RequestRepository requestRepository;

    private void insertRequest(String transactionId, boolean isValid, String fromService, String toService) {
        requestRepository.save(
                new Request(
                        transactionId, isValid,
                        Instant.now().toString(),
                        fromService, toService));
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

    @GetMapping("/error")
    public ResponseEntity<String> handleError(Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        insertRequest(UUID.randomUUID().toString(), false, "UiApi", uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody String json, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject(json);
        String transactionId = jsonObject.getString("transactionId");
        ObjectMapper mapper = new ObjectMapper();
        User userData = null, user;
        try {
            userData = mapper.readValue(jsonObject.getJSONObject("userData").toString(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user = repo.save(userData);
        if (user != null) {
            insertRequest(transactionId, true, request.getRequestURI(), "--");
            return ResponseEntity.status(HttpStatus.OK).body("User added successfully");
        }
        insertRequest(UUID.randomUUID().toString(), false, request.getRequestURI(), "--");
        throw new RuntimeException("User DB insertion failed");
    }
}