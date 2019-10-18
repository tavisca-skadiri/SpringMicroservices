package com.tavisca.gce.DBValidatorApi;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidateService {
    boolean isValid(User user) {
        return isPasswordValid(user);
    }

    private boolean isPasswordValid(User user) {
        String regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
        String password = user.getPassword();
        return Pattern.compile(regex)
                .matcher(password)
                .matches();
    }
}