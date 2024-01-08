package com.example.demo.JobApplication.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@Entity(name = "JOB")
public class Job {

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
    @NotNull
    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;
    @NotNull
    @Column(name = "ModifiedDate")
    private LocalDateTime modifiedDate;
    @Column(name = "JobStatus")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    public Job() {

    }

    public Job(Long jobID, String jobName, String description, LocalDateTime createdDate, LocalDateTime modifiedDate, JobStatus status) {
        this.jobID = jobID;
        this.jobName = jobName;
        this.description = description;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.status = status;
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

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() { return createdDate; }

    public void setCreatedDate(LocalDateTime createdDate) {
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


    @Override
    public String toString() {
        return "Job{" + "jobID=" + jobID + ", jobName='" + jobName + '\'' + ", description='" + description + '\'' + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", status=" + status + '}';
    }

}
