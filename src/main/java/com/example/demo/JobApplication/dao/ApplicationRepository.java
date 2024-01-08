package com.example.demo.JobApplication.dao;

import com.example.demo.JobApplication.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {


}
