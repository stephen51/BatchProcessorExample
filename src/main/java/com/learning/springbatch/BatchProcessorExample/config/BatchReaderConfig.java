package com.learning.springbatch.BatchProcessorExample.config;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;

@Configuration
public class BatchReaderConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Value("${output.filepath}")
    private String outputFilePath;

    @Autowired
    private EmployeeRowMapper employeeRowMapper;

    @Autowired
    private CustomSkipListener customSkipListener;

    @Bean
    public Job fetchItDepartmentJob() {
        return new JobBuilder("fetchItDepartmentJob",jobRepository)
                .start(step1())
                //.listener(customSkipListener)
                .build();

    }

    @Bean
    public Step step1(){
        return new StepBuilder("step1",jobRepository)
                .<Employee,Employee>chunk(1000,transactionManager)
                .reader(empReader())
                .processor(empProcessor())
                .writer(empWriter())
                .listener(customSkipListener)
                .build();
    }

    @Bean
    public FlatFileItemWriter<Employee> empWriter() {
        return new FlatFileItemWriterBuilder<Employee>()
                .name("emp-writer")
                .resource(new FileSystemResource(outputFilePath))
                .shouldDeleteIfExists(true)
                .delimited()
                .delimiter(",")
                .names("id","firstName","lastName","email","department","salary","hireDate")
                .headerCallback(getCallback())
                .build();
    }

    private FlatFileHeaderCallback getCallback() {
        return new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write("id,first_name,last_name,email,department,salary,hire_date");
            }
        };
    }

    @Bean
    public ItemProcessor<? super Employee,? extends Employee> empProcessor() {
        return new ItemProcessor<Employee, Employee>() {
            @Override
            public Employee process(Employee employee) throws Exception {
                // perform any necessary processing on the employee object
                // for example, logging, updating employee details, etc.
                return employee;
            }
        };
    }

    @Bean
    public JdbcCursorItemReader<Employee> empReader() {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("emp-reader")
                .dataSource(dataSource)
                .sql(" select * from employees_info where department='IT' order by salary")
                .connectionAutoCommit(false)
                .rowMapper(employeeRowMapper)
                .build();
    }
}
