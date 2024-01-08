package com.example.demo.JobApplication.controller;

import com.example.demo.JobApplication.entity.Job;
import com.example.demo.JobApplication.exception.ResourceNotFoundException;
import com.example.demo.JobApplication.service.JobServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    private final JobServiceImpl jobService;

    public JobController(JobServiceImpl jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Job>> getAllJobs() {
        return jobService.findAll();
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<String> getJobById(@PathVariable Long jobId) {

        ResponseEntity<Job> jobResponse = jobService.getJobById(jobId);
        if (jobResponse.getStatusCode() == HttpStatus.OK) {
            return new ResponseEntity<>("job id found", HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("/{jobID}")
    public ResponseEntity<String> updateJob(@PathVariable Long jobID, @RequestBody Job updatedata) {
        return jobService.updateJob(jobID, updatedata);

    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId) {
        return jobService.deleteJob(jobId);
    }

    @DeleteMapping("/inactive")
    public ResponseEntity<String> deleteInactive() {
        return jobService.deleteInactive();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllData() {
        return jobService.deleteAllData();
    }


}
