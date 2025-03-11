package com.learning.springbatch.BatchProcessorExample;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessorExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchProcessorExampleApplication.class, args);
	}

}
