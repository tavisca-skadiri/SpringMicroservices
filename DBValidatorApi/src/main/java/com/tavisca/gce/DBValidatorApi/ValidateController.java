package com.tavisca.gce.DBValidatorApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidateController {
    @Autowired
    private UserRepository repo;

    @PostMapping("/validate")
    public ResponseEntity addUser(@RequestBody User userData) {

        return ResponseEntity.status(HttpStatus.OK).body(repo.save(userData));
    }
}