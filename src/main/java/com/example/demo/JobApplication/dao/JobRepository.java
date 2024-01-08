package com.example.demo.JobApplication.dao;

import com.example.demo.JobApplication.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(Enum status);
    @Query("SELECT j FROM JOB j WHERE DATE_FORMAT(j.createdDate, '%d-%m-%Y') = :createdDate")
    List<Job> findByCreatedDateCustomQuery(String createdDate);
}

