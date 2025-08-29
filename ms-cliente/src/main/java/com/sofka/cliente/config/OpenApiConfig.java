package com.sofka.cliente.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("API de Clientes y Personas")
						.version("1.0.0")
						.description("Esta API gestiona la información de los clientes y personas. Es mantenida por Sofka.")
						.contact(new Contact()
								.name("Sofka Technologies")
								.email("api@sofka.com.ec")
								.url("https://www.sofka.com.ec")));
	}
}