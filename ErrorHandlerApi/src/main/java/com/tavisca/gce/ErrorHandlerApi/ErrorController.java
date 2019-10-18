package com.tavisca.gce.ErrorHandlerApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;

@ControllerAdvice
@RestController
public class ErrorController extends ResponseEntityExceptionHandler {

    @Autowired
    ExceptionLoggerRepository repo;

    @PostMapping("/exceptionlogger")
    public String handleAllExceptions(@RequestBody Exception ex) {
        repo.save(new ExceptionResponse(new Date(),ex.getMessage(),ex.toString()));
        System.out.println(ex.getMessage());
        return ex.getMessage();
    }

    @GetMapping("/exceptions")
    public List<ExceptionResponse> getExceptions() {
        return repo.findAll();
    }

}