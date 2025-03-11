package com.learning.springbatch.BatchProcessorExample.config;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import com.learning.springbatch.BatchProcessorExample.repository.EmployeesRepo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeItemWriter implements ItemWriter<Employee> {

    private final EmployeesRepo employeesRepo;
    public EmployeeItemWriter(EmployeesRepo employeesRepo){
        this.employeesRepo = employeesRepo;
    }

    @Override
    public void write(Chunk<? extends Employee> chunk) throws Exception {
        employeesRepo.saveAll(chunk);
    }

}
