package com.example.demo.JobApplication.controller;


import com.example.demo.JobApplication.service.ApplicationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/application")
public class ApplicationController {

private final ApplicationServiceImpl applicationservice;


    public ApplicationController(ApplicationServiceImpl applicationservice) {
        this.applicationservice = applicationservice;
    }
    @GetMapping("/datefilter/{createdDate}")
    public ResponseEntity<String> filterJobsByDate(@PathVariable String createdDate){
        return applicationservice.filterJobByCreatedDate(createdDate);
    }
}
