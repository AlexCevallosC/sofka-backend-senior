package com.sofka.cuenta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.cuenta.MsCuentaApplication;
import com.sofka.cuenta.TestcontainersConfig;
import com.sofka.cuenta.repository.CuentaRepository;
import com.sofka.commons.model.Cuenta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = MsCuentaApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(TestcontainersConfig.class)
@AutoConfigureMockMvc
public class CuentaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CuentaRepository cuentaRepository;

	private Cuenta cuentaTest;

	@BeforeEach
	void setUp() {
		//cuentaRepository.deleteAll();

		cuentaTest = new Cuenta();
		cuentaTest.setNumeroCuenta("478758");
		cuentaTest.setTipoCuenta("Ahorros");
		cuentaTest.setSaldoInicial(new BigDecimal("5000.00"));
		cuentaTest.setEstado(true);
		cuentaTest.setClienteIdentificacion("1054879");
		cuentaRepository.save(cuentaTest);
	}

	@Test
	void testObtenerCuentas_Retorna200() throws Exception {
		mockMvc.perform(get("/api/v1/cuentas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty());
	}

	@Test
	void testObtenerCuentasPorId_Retorna200() throws Exception {
		mockMvc.perform(get("/api/v1/cuentas/{id}", cuentaTest.getNumeroCuenta()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.numeroCuenta").value(cuentaTest.getNumeroCuenta()))
				.andExpect(jsonPath("$.tipoCuenta").value(cuentaTest.getTipoCuenta()));
	}

}