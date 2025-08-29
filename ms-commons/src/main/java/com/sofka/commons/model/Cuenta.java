package com.sofka.commons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "cuenta")
public class Cuenta {
	@Id
	private String numeroCuenta;
	private String tipoCuenta;
	private BigDecimal saldoInicial;
	private boolean estado;
	private String clienteIdentificacion; // Relación con Cliente
}