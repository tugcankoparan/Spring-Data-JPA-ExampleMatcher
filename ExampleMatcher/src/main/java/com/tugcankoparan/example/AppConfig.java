package com.tugcankoparan.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@EnableJpaRepositories
@ComponentScan
@Configuration
public class AppConfig {

    @Bean
    EntityManagerFactory entityManagerFactory() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("example-unit");
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }
}