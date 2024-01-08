package com.example.demo.JobApplication.service;

import com.example.demo.JobApplication.dao.JobRepository;
import com.example.demo.JobApplication.entity.Job;
import com.example.demo.JobApplication.exception.InvalidJobIdException;
import com.example.demo.JobApplication.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.JobApplication.entity.JobStatus.INACTIVE;


@Service
public class JobServiceImpl implements JobService {

private final JobRepository jobRepository;
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);


    @Override
    public ResponseEntity<List<Job>> findAll() {
        List<Job> jobs = jobRepository.findAll().stream()
                .sorted(Comparator.comparing(Job::getCreatedDate).reversed())
                .collect(Collectors.toList());
        if (jobs.isEmpty()) {
            logger.info("No jobs found in the repository.");
            return new ResponseEntity(Collections.emptyList(), HttpStatus.NO_CONTENT);

        }
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Job> getJobById(Long jobID) {
        try {
            Job job = jobRepository.findById(jobID)
                    .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobID));
            logger.info("Retrieved job by id: {}", jobID);
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving job with id: {}", jobID, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> createJob(Job job) {
        logger.info("Received request to create job: {}", job.getJobID());
        try {
            job.setCreatedDate(LocalDateTime.now());
            job.setModifiedDate(LocalDateTime.now());

            Job savedJob = jobRepository.save(job);

            logger.info("Created job with id: {}", savedJob.getJobID());

            return new ResponseEntity<>("Job created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create job. Check the input data or log for more details.", e);
            return new ResponseEntity<>("Validation errors occurred ", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<String> updateJob(Long jobID, Job update) {
        logger.info("Received request to update job:{}", jobID);
        try {
            Job existingJob = getJobById(jobID).getBody();
            existingJob.setJobName(update.getJobName());
            existingJob.setDescription(update.getDescription());
            existingJob.setModifiedDate(LocalDateTime.now());
            existingJob.setStatus(update.getStatus());
            Job savedJob = jobRepository.save(existingJob);
            logger.info("Updated job with id: {}", savedJob.getJobID());
            return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to update job. Check the input data or log for more details.", e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteJob(Long jobID) {

        logger.info("Received request to delete job: {}", jobID);

            Optional<Job> optionalJob = jobRepository.findById(jobID);
            if (optionalJob.isPresent()) {
                jobRepository.deleteById(jobID);
                logger.info("Job deleted successfully: {}", jobID);
                return new ResponseEntity<>("Job deleted successfully", HttpStatus.OK);
            } else {
                logger.error("Job with ID {} not found", jobID);
                throw new ResourceNotFoundException("Job not found with id: " + jobID);
            }

    }

    public List<Job> getJobsByStatus(Enum status) {
        return jobRepository.findByStatus(status);
    }

    @Override
    public ResponseEntity<String> deleteInactive() {
        try {
            List<Job> inactiveJobs = getJobsByStatus(INACTIVE);

            inactiveJobs.forEach(job -> {
                jobRepository.delete(job);
            });
            logger.info("Jobs with INACTIVE status deleted successfully");
            return new ResponseEntity<>("Jobs with INACTIVE status deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("An error occurred: " + e.getMessage(), e);
            return new ResponseEntity<>("An error occurred: " , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> deleteAllData() {
        try {
            jobRepository.deleteAll();
            return new ResponseEntity<>("All data deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}