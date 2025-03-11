package com.learning.springbatch.BatchProcessorExample.config;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import com.learning.springbatch.BatchProcessorExample.repository.EmployeesRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Value("${input.filepath}")
    private String inputFilePath;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Autowired
    private EmployeeItemProcessor employeeProcessor;

    @Autowired
    private JobCompletionNotificationListener listener;

    @Bean
    //@JobScope
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    //@StepScope
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                     JdbcBatchItemWriter<Employee> writer){
        return new StepBuilder("step",jobRepository)
                .<Employee,Employee>chunk(1000, transactionManager)
                .reader(reader())
                .processor(employeeProcessor)
                .writer(writer)
                .build();
    }



    @Bean
    @StepScope
    public FlatFileItemReader<Employee> reader(){
        return new FlatFileItemReaderBuilder<Employee>()
                .name("Employee-reader")
                .resource(new FileSystemResource(inputFilePath))
                .delimited()
                .names("id","first_name","last_name","email","department","salary","hire_date")
                .strict(true)
                .fieldSetMapper(new EmployeeFieldMapper())
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Employee> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Employee>()
                .sql("INSERT INTO employees_info (id, first_name, last_name, email, department, salary, hire_date) values (:id, :firstName, :lastName, :email, :department, :salary, :hireDate)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public RepositoryItemWriter<Employee> employeeRepositoryItemWriter(){
        return new RepositoryItemWriterBuilder<Employee>()
                .repository(employeesRepo)
                .methodName("saveAll")
                .build();
    }


}
