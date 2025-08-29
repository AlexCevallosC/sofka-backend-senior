package com.sofka.cuenta.controller;

import com.sofka.commons.model.Cliente;
import com.sofka.commons.model.Cuenta;
import com.sofka.commons.model.Movimiento;
import com.sofka.cuenta.service.ICuentaService;
import com.sofka.cuenta.service.dto.ReporteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Cuentas y Movimientos", description = "Endpoints para la gestión de cuentas, movimientos y reportes")
public class CuentaController {

	@Autowired
	private ICuentaService cuentaService;

	// F1 - CRUD de Cuentas
	@GetMapping("/cuentas")
	@Operation(summary = "Obtener todas las cuentas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Cuenta.class)))
	})
	public ResponseEntity<List<Cuenta>> obtenerCuentas() {
		return ResponseEntity.ok(cuentaService.obtenerTodasLasCuentas());
	}

	@GetMapping("/cuentas/{id}")
	@Operation(summary = "Obtener una cuenta por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuenta obtenido exitosamente"),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrado")
	})
	public ResponseEntity<Cuenta> obtenerClientePorId(@PathVariable String id) {
		return ResponseEntity.ok(cuentaService.obtenerCuentaPorNumero(id));
	}


	@PostMapping("/cuentas")
	@Operation(summary = "Crear una nueva cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Cuenta.class)))
	})
	public ResponseEntity<Cuenta> crearCuenta(@RequestBody Cuenta cuenta) {
		return ResponseEntity.status(201).body(cuentaService.crearCuenta(cuenta));
	}

	@PutMapping("/cuentas/{numeroCuenta}")
	@Operation(summary = "Actualizar una cuenta existente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuenta actualizada exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Cuenta.class))),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
	})
	public ResponseEntity<Cuenta> actualizarCuenta(@PathVariable String numeroCuenta, @RequestBody Cuenta cuenta) {
		return ResponseEntity.ok(cuentaService.actualizarCuenta(numeroCuenta, cuenta));
	}

	@DeleteMapping("/cuentas/{numeroCuenta}")
	@Operation(summary = "Eliminar una cuenta por número")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
	})
	public ResponseEntity<Void> eliminarCuenta(@PathVariable String numeroCuenta) {
		cuentaService.eliminarCuenta(numeroCuenta);
		return ResponseEntity.noContent().build();
	}

	// F2 y F3 - Movimientos
	@PostMapping("/movimientos")
	@Operation(summary = "Registrar un movimiento (depósito o retiro)")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Movimiento registrado exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = Movimiento.class))),
			@ApiResponse(responseCode = "400", description = "Saldo no disponible o tipo de movimiento inválido")
	})
	public ResponseEntity<?> registrarMovimiento(@RequestBody Movimiento movimiento) {
		try {
			Movimiento nuevoMovimiento = cuentaService.registrarMovimiento(movimiento);
			return ResponseEntity.status(201).body(nuevoMovimiento);
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// F4 - Reportes
	@GetMapping("/reportes")
	@Operation(summary = "Generar un reporte de movimientos por cliente y rango de fechas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ReporteDTO.class)))
	})
	public ResponseEntity<ReporteDTO> generarReporte(
			@RequestParam String clienteId,
			@RequestParam String fechaInicio,
			@RequestParam String fechaFin) {
		return ResponseEntity.ok(cuentaService.generarReporte(clienteId, fechaInicio, fechaFin));
	}
}