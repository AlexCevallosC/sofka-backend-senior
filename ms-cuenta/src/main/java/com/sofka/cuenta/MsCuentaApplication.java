package com.sofka.cuenta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sofka.cuenta", "com.sofka.commons"})
@EnableJpaRepositories(basePackages = "com.sofka.cuenta.repository")
@EntityScan(basePackages = "com.sofka.commons.model")
public class MsCuentaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsCuentaApplication.class, args);
    }
}
