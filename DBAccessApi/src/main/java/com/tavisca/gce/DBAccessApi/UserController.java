package com.tavisca.gce.DBAccessApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.status(HttpStatus.OK).body(repo.findById(id));
    }
}