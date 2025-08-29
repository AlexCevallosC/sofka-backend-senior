package com.sofka.cuenta.config;

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
						.title("API de Cuentas y Movimientos")
						.version("1.0.0")
						.description("Esta API gestiona las cuentas bancarias, los movimientos y los reportes financieros. Es mantenida por Sofka.")
						.contact(new Contact()
								.name("Sofka Technologies")
								.email("api@sofka.com.ec")
								.url("https://www.sofka.eca")));
	}
}