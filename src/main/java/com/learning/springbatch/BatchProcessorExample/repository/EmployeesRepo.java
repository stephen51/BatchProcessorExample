package com.learning.springbatch.BatchProcessorExample.repository;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepo extends JpaRepository<Employee,Long> {
}
