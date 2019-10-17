package com.tavisca.gce.ErrorHandlerApi;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {
    @PostMapping("/exceptionlogger")
    public String handleAllExceptions(@RequestBody String ex) {
        System.out.println(ex);
        return ex;
    }
}