package com.sofka.cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sofka.cliente", "com.sofka.commons"})
@EnableJpaRepositories(basePackages = "com.sofka.cliente.repository")
@EntityScan(basePackages = "com.sofka.commons.model")

public class MsClienteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsClienteApplication.class, args);
    }
}
