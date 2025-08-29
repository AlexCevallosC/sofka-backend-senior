package com.sofka.cuenta.service;

import com.sofka.commons.model.Cuenta;
import com.sofka.commons.model.Movimiento;
import com.sofka.cuenta.service.dto.ReporteDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ICuentaService {
	Cuenta crearCuenta(Cuenta cuenta);
	Cuenta obtenerCuentaPorNumero(String numeroCuenta);
	List<Cuenta> obtenerTodasLasCuentas();
	Cuenta actualizarCuenta(String numeroCuenta, Cuenta cuenta);
	void eliminarCuenta(String numeroCuenta);
	Movimiento registrarMovimiento(Movimiento movimiento);
	List<Movimiento> obtenerMovimientosPorNumeroYrangoFechas(String numeroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);
	ReporteDTO generarReporte(String clienteId, String fechaInicio, String fechaFin);
}