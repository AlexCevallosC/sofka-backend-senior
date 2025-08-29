package com.sofka.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.cliente.MsClienteApplication;
import com.sofka.cliente.TestcontainersConfig;
import com.sofka.commons.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MsClienteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCrearCliente_Retorna201() throws Exception {
		// Given
		Cliente nuevoCliente = new Cliente();
		nuevoCliente.setNombre("Juan Perez");

		nuevoCliente.setIdentificacion("1054880");

		// When & Then
		mockMvc.perform(post("/api/v1/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(nuevoCliente)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.nombre").value("Juan Perez"));
	}

	@Test
	void testObtenerClientePorId_Retorna200() throws Exception {
		mockMvc.perform(get("/api/v1/clientes/{id}", "1054880"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identificacion").value("1054880"))
				.andExpect(jsonPath("$.estado").value(true));
	}

}