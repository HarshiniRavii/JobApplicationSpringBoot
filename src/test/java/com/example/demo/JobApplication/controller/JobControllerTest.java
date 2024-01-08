
package com.example.demo.JobApplication.controller;

import com.example.demo.JobApplication.entity.Job;
import com.example.demo.JobApplication.entity.JobStatus;
import com.example.demo.JobApplication.service.JobService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    @MockBean
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllJobsWithJobsFound() throws Exception {
        Job job1 = new Job(1L, "Job 1", "Description 1", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE);
        Job job2 = new Job(2L, "Job 2", "Description 2", LocalDateTime.now(), LocalDateTime.now(), JobStatus.INACTIVE);
        List<Job> mockJobs = Arrays.asList(job1, job2);

        when(jobService.findAll()).thenReturn((ResponseEntity<List<Job>>) mockJobs);

        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();

        mockMvc.perform(get("/job/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].jobID").value(1))
                .andExpect(jsonPath("$[0].jobName").value("Job 1"))
                .andExpect(jsonPath("$[1].jobID").value(2))
                .andExpect(jsonPath("$[1].jobName").value("Job 2"));

        verify(jobService, times(1)).findAll();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testGetJobById() throws Exception {

        Long jobId = 54L;
        Job mockJob = new Job(jobId, "Developer", "JAVALanguage", LocalDateTime.now(), LocalDateTime.now(), JobStatus.INACTIVE);

//        ObjectMapper objectMapper = new ObjectMapper();
//        String mockJobJson = objectMapper.writeValueAsString(mockJob);
//
//        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(mockJobJson, HttpStatus.OK);
//        when(jobService.getJobById(jobId)).thenReturn(mockResponseEntity);

        ResponseEntity<Job> mockResponseEntity = new ResponseEntity<>(mockJob, HttpStatus.OK);
        when(jobService.getJobById(jobId)).thenReturn(mockResponseEntity);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/job/{jobId}", jobId)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobID").value(jobId))
                .andExpect(jsonPath("$.jobName").value("Developer"))
                .andExpect(jsonPath("$.description").value("JAVALanguage"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));

        verify(jobService, times(1)).getJobById(jobId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testCreateJob() throws Exception {

        Long jobId = 1L;
        Job mockJob = new Job(jobId, "TestJob", "Description", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE);

        when(jobService.createJob(any(Job.class))).thenReturn(new ResponseEntity<>("Job created successfully", HttpStatus.CREATED));


        mockMvc.perform(post("/job/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jobID\":1,\"jobName\":\"TestJob\",\"description\":\"Description\",\"status\":\"ACTIVE\"}")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isCreated())
                .andExpect(content().string("Job created successfully"));

        verify(jobService, times(1)).createJob(any(Job.class));
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testUpdateJob() throws Exception {

        Long jobId = 1L;
        Job updatedJob = new Job(jobId, "UpdatedJobName", "UpdatedDescription", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE);

        when(jobService.updateJob(eq(jobId), any(Job.class))).thenReturn(new ResponseEntity<>("Job updated successfully", HttpStatus.OK));

        mockMvc.perform(put("/job/{jobID}", jobId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jobID\":1,\"jobName\":\"UpdatedJobName\",\"description\":\"UpdatedDescription\",\"status\":\"ACTIVE\"}")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("Job updated successfully"));

        verify(jobService, times(1)).updateJob(eq(jobId), any(Job.class));
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testDeleteJob() throws Exception {

        Long jobId = 1L;

        when(jobService.deleteJob(jobId)).thenReturn(new ResponseEntity<>("Job deleted successfully", HttpStatus.OK));


        mockMvc.perform(delete("/job/{jobId}", jobId)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("Job deleted successfully"));


        verify(jobService, times(1)).deleteJob(jobId);
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testDeleteInactive() throws Exception {

        when(jobService.deleteInactive()).thenReturn(new ResponseEntity<>("Inactive jobs deleted successfully", HttpStatus.OK));


        mockMvc.perform(delete("/job/inactive")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("Inactive jobs deleted successfully"));


        verify(jobService, times(1)).deleteInactive();
        verifyNoMoreInteractions(jobService);
    }

    @Test
    public void testDeleteAllData() throws Exception {

        when(jobService.deleteAllData()).thenReturn(new ResponseEntity<>("All data deleted successfully", HttpStatus.OK));


        mockMvc.perform(delete("/job/delete")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("harshini:hari123".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("All data deleted successfully"));


        verify(jobService, times(1)).deleteAllData();
        verifyNoMoreInteractions(jobService);
    }
}
