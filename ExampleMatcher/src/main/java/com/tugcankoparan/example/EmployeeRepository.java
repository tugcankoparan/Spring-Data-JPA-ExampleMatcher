package com.tugcankoparan.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface EmployeeRepository extends CrudRepository<Employee, Long>,
        QueryByExampleExecutor<Employee> {
}