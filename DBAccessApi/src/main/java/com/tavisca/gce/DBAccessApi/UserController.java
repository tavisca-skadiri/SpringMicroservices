package com.tavisca.gce.DBAccessApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository repo;

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(repo.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable("id") Integer id) {
        Optional<User> user;
        user = repo.findById(id);
        user.orElseThrow(()->new RuntimeException("User not found"));
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/error")
    public ResponseEntity<String> handleError(Exception e) {
        final URI uri = URI.create("http://localhost:6060/exceptionlogger/");
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, e, String.class);
    }

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody User userData) {
        User user = repo.save(userData);
        if(user!=null)
            return ResponseEntity.status(HttpStatus.OK).body(repo.save(user));
        throw new RuntimeException("User DB insertion failed");
    }
}