package com.sofka.cuenta.service.dto;

import com.sofka.commons.model.Movimiento;
import com.sofka.commons.model.Cuenta;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReporteDTO {
	private LocalDateTime fecha;
	private String clienteNombre;
	private List<CuentaReporte> cuentas;

	@Data
	public static class CuentaReporte {
		private String numeroCuenta;
		private String tipoCuenta;
		private BigDecimal saldoInicial;
		private boolean estado;
		private List<Movimiento> movimientos;
	}
}