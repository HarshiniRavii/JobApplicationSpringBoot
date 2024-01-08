package com.example.demo.JobApplication.service;

import com.example.demo.JobApplication.dao.JobRepository;
import com.example.demo.JobApplication.entity.Job;
import com.example.demo.JobApplication.entity.JobStatus;
import com.example.demo.JobApplication.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.demo.JobApplication.entity.JobStatus.INACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class JobServiceImplTest {

    @InjectMocks
    private JobServiceImpl jobService;


    @Mock
    private JobRepository jobRepository;

    @Test
    public void testFindAllJobs() {

        Job job1 = new Job(9L, "TestJob", "Description", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE);
        Job job2 = new Job(7L, "TestJob", "Description", LocalDateTime.now(), LocalDateTime.now(), JobStatus.INACTIVE);

        jobRepository.saveAll(Arrays.asList(job1, job2));

        ResponseEntity<List<Job>> responseEntity = jobService.findAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Arrays.asList(job1, job2), responseEntity.getBody());
    }

    @Test
    public void testFindAllJobsNotFound() {
        ResponseEntity<List<Job>> responseEntity = jobService.findAll();
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
    }

    @Test
    public void testGetJobById() {

        Long jobId = 67L;
        Job mockJob = new Job(jobId, "TestJob", "Description", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(mockJob));

        ResponseEntity<Job> responseEntity = jobService.getJobById(jobId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockJob, responseEntity.getBody());
    }

    @Test
    public void testGetJobByIdNotFound() throws Exception {

        Long jobId = 1L;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        ResponseEntity<Job> responseEntity = jobService.getJobById(jobId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(null, responseEntity.getBody());
    }

    @Test
    public void testCreateJob_Success() {

        Job inputJob = new Job(null, "TestJob", "Description", null, null, JobStatus.INACTIVE);

        when(jobRepository.save(Mockito.any(Job.class))).thenAnswer(invocation -> {
            Job savedJob = invocation.getArgument(0);
            savedJob.setJobID(1L);
            savedJob.setCreatedDate(LocalDateTime.now());
            savedJob.setModifiedDate(LocalDateTime.now());
            return savedJob;
        });

        ResponseEntity<String> responseEntity = jobService.createJob(inputJob);

        assertNotNull(responseEntity);
        assertEquals("Job created successfully", responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
//500-201
   public void testCreateJob_Failure() {
        Job job = new Job();
        job.setJobName(" ");
        job.setDescription("Short");
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        ResponseEntity<String> responseEntity = jobService.createJob(job);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Validation errors occurred:"));
        assertTrue(responseEntity.getBody().contains("Name is mandatory"));
        assertTrue(responseEntity.getBody().contains("Description must be more than 5 characters"));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    public void testUpdateJob_Success() {
        when(jobRepository.save(any())).thenReturn(new Job(27L, "Mocked Job", "Mocked Description", LocalDateTime.now(), LocalDateTime.now(), JobStatus.ACTIVE));

        ResponseEntity<String> response = jobService.updateJob(27L, new Job(7L, "Updated Job Name", "Updated Description", LocalDateTime.now(), LocalDateTime.now(), INACTIVE));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Job updated successfully", response.getBody());
    }

    @Test
    public void testUpdateJob_Failure() {
        when(jobRepository.save(any())).thenThrow(new RuntimeException("Simulated error"));

        ResponseEntity<String> response = jobService.updateJob(1L, new Job());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred", response.getBody());
    }

    @Test
    public void testDeleteJobByIdSuccess() {

        Long jobId = 45L;

        doNothing().when(jobRepository).deleteById(jobId);

        ResponseEntity<String> result = jobService.deleteJob(jobId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Job deleted successfully", result.getBody());

        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    public void testDeleteJobByIdInvalidJobId() {
        Long invalidJobId = 101L;

        ResponseEntity<String> result = jobService.deleteJob(invalidJobId);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid Job ID", result.getBody());

        verify(jobRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testDeleteJobByIdFailure() {

        Long jobId = 25L;

        doThrow(new RuntimeException("Simulated internal server error")).when(jobRepository).deleteById(jobId);

        ResponseEntity<String> result = jobService.deleteJob(jobId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("An error occurred", result.getBody());

        verify(jobRepository, times(1)).deleteById(jobId);
    }

    @Test
    public void testDeleteInactive_Success() {

        List<Job> inactiveJobs = new ArrayList<>();

        when(jobRepository.findByStatus(INACTIVE)).thenReturn(inactiveJobs);

        ResponseEntity<String> response = jobService.deleteInactive();

        verify(jobRepository, times(inactiveJobs.size())).delete(any(Job.class));
        assertEquals("Jobs with INACTIVE status deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteInactive_Failure() {
        when(jobRepository.findByStatus(INACTIVE)).thenThrow(new RuntimeException("Simulated error"));

        ResponseEntity<String> response = jobService.deleteInactive();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }

    @Test
    public void testDeleteAllData_Success() {
        doNothing().when(jobRepository).deleteAll();

        ResponseEntity<String> response = jobService.deleteAllData();

        verify(jobRepository, times(1)).deleteAll();

        assertEquals("All data deleted successfully", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAllData_Failure() {
        doThrow(new RuntimeException("Simulated error")).when(jobRepository).deleteAll();

        ResponseEntity<String> response = jobService.deleteAllData();

        verify(jobRepository, times(1)).deleteAll();

        assertEquals("An error occurred", response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
