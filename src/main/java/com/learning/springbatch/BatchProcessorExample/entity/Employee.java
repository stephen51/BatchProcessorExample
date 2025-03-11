package com.learning.springbatch.BatchProcessorExample.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "employees_info")
public class Employee {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "department")
    private String department;
    @Column(name = "salary")
    private Double salary;
    @Column(name = "hire_date")
    private String hireDate;
}
