package com.sofka.commons.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "movimiento")
public class Movimiento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime fecha;
	private String tipoMovimiento; // "Retiro" o "Depósito"
	private BigDecimal valor;
	private BigDecimal saldo;
	private String numeroCuenta; // Relación con Cuenta
}