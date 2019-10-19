package com.tavisca.gce.ErrorHandlerApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class ErrorController {

    @Autowired
    private ExceptionLoggerRepository repo;

    @PostMapping("/exceptionlogger")
    public String handleAllExceptions(@RequestBody Exception ex, HttpServletRequest request) {
        repo.save(new ExceptionResponse(new Date(), ex.getMessage(), request.getRequestURI()));
        System.out.println(ex.getMessage());
        return ex.getMessage();
    }

    @GetMapping("/exceptions")
    public List<ExceptionResponse> getExceptions() {
        return repo.findAll();
    }
}