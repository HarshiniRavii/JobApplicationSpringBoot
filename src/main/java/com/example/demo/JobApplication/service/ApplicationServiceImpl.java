package com.example.demo.JobApplication.service;

import com.example.demo.JobApplication.dao.ApplicationRepository;
import com.example.demo.JobApplication.dao.JobRepository;
import com.example.demo.JobApplication.entity.Application;
import com.example.demo.JobApplication.entity.ApplicationStatus;
import com.example.demo.JobApplication.entity.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.demo.JobApplication.entity.ApplicationStatus.APPLIED;
import static com.example.demo.JobApplication.entity.ApplicationStatus.PENDING;

@Service
public class ApplicationServiceImpl implements ApplicationService {

private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    public ApplicationServiceImpl(JobRepository jobRepository, ApplicationRepository applicationRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }
    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    public ResponseEntity<String> filterJobByCreatedDate(String createdDate) {
        try {
            List<Job> jobs = jobRepository.findByCreatedDateCustomQuery((String.valueOf(createdDate)));
            if (jobs.size() > 0) {
                for (Job job : jobs) {
                    Application application = createApplicationFromJob(job,APPLIED);
                    applicationRepository.save(application);
                }
                return new ResponseEntity<>("Data copied successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No data found", HttpStatus.NOT_FOUND);
            }
        }
        catch(Exception e)
        {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Application createApplicationFromJob(Job job, ApplicationStatus applicationStatus) {
        try {
            Application application = new Application();
            application.setApplicationStatus(applicationStatus);
            application.setJobID(job.getJobID());
            application.setjobName(job.getJobName());
            LocalDateTime originalCreatedDate = job.getCreatedDate();
            String formattedDate = originalCreatedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            application.setCreatedDate(formattedDate);
            application.setDescription(job.getDescription());
            application.setModifiedDate(job.getModifiedDate());
            application.setStatus(job.getStatus());
            return application;
        } catch (Exception e) {
            throw e;
        }
    }
}