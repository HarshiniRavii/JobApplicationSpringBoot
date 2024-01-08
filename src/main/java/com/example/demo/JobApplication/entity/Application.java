package com.example.demo.JobApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Entity(name = "ApplicationStatus")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JobId")
    @NotNull
    public Long jobID;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "JobName")
    private String jobName;

    @Size(max = 150, message = "Description can't be more than 150 characters")
    @Size(min = 5, message = "Description must be more than 5 characters")
    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedDate")
    private String createdDate;

    @NotNull
    @Column(name = "ModifiedDate")
    private LocalDateTime modifiedDate;

    @Column(name = "JobStatus")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Column(name = "ApplicationStatus")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    public Application() {
    }

    public Application(Long jobID, String jobName, String description, String createdDate, LocalDateTime modifiedDate, JobStatus status, ApplicationStatus applicationStatus) {
        this.jobID = jobID;
        this.jobName = jobName;
        this.description = description;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
        this.applicationStatus = applicationStatus;
    }

    public Long getJobID() {
        return jobID;
    }

    public void setJobID(Long jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setjobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public String toString() {
        return "Application{" +
                "jobID=" + jobID +
                ", jobName='" + jobName + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", status=" + status +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
