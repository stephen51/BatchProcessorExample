package com.learning.springbatch.BatchProcessorExample.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/batch")
public class BatchController {

    @Value("${input.filepath}")
    private String inputFilePath;

    @Value("${output.filepath}")
    private String outputFilePath;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private Job fetchItDepartmentJob;

    @GetMapping("/start")
    public String readerBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters params = new JobParametersBuilder()
                .addString("startAt", new Timestamp(System.currentTimeMillis()).toString())
                .addString("filepath",inputFilePath)
                .toJobParameters();
        jobLauncher.run(job,params);
        return "Batch Completed successfully";
    }

    @GetMapping("/fetchItDepartment")
    public String writerBatch() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters params = new JobParametersBuilder()
                .addString("startAt", new Timestamp(System.currentTimeMillis()).toString())
                .addString("filepath",outputFilePath)
                .toJobParameters();
        jobLauncher.run(fetchItDepartmentJob,params);
        return "Batch Completed successfully";
    }
}
