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
import org.springframework.batch.repeat.RepeatOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

    @Autowired
    private EmployeeItemWriter employeeItemWriter;

    @Bean
    public Job job(JobRepository jobRepository, Step step){
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager,
                     JdbcBatchItemWriter<Employee> writer){
        return new StepBuilder("step",jobRepository)
                .<Employee,Employee>chunk(1000, transactionManager)
                .reader(reader())
                .processor(employeeProcessor)
                .writer(writer)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);  // Define number of concurrent threads
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
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
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeesRepo);
        writer.setMethodName("saveAll");
        return writer;
    }


}
