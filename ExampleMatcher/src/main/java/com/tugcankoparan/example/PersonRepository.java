package com.tugcankoparan.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface PersonRepository extends CrudRepository<Person, Long>,
        QueryByExampleExecutor<Person> {
}