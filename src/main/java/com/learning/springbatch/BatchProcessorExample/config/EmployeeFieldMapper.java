package com.learning.springbatch.BatchProcessorExample.config;

import com.learning.springbatch.BatchProcessorExample.entity.Employee;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EmployeeFieldMapper implements FieldSetMapper<Employee> {
    @Override
    public Employee mapFieldSet(FieldSet fieldSet) {
        Employee employee = new Employee();
        employee.setId(fieldSet.readLong(0));
        employee.setFirstName(fieldSet.readString(1));
        employee.setLastName(fieldSet.readString(2));
        employee.setEmail(fieldSet.readString(3));
        employee.setDepartment(fieldSet.readString(4));
        employee.setSalary(fieldSet.readDouble(5));
        employee.setHireDate(fieldSet.readString(6));

        return employee;
    }
}
