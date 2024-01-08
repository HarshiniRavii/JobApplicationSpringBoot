package com.example.demo.JobApplication.service;

import com.example.demo.JobApplication.entity.Job;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface JobService {

    public ResponseEntity <List<Job> >findAll();

    public ResponseEntity<Job> getJobById(Long jobID);

    public ResponseEntity<String> createJob(Job job);

    public ResponseEntity<String> updateJob(Long jobID, Job update);

    public ResponseEntity<String> deleteJob(Long jobID);

    public ResponseEntity<String> deleteInactive();

    public ResponseEntity<String> deleteAllData();

}
