package com.sofka.cliente.controller;

import com.sofka.commons.model.Cliente;
import com.sofka.cliente.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Endpoints para la gestión de clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping
	@Operation(summary = "Obtener todos los clientes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
	})
	public ResponseEntity<List<Cliente>> obtenerClientes() {
		return ResponseEntity.ok(clienteService.obtenerTodosLosClientes());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener un cliente por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente obtenido exitosamente"),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	})
	public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo cliente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cliente creado exitosamente")
	})
	public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
		return ResponseEntity.status(201).body(clienteService.crearCliente(cliente));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar un cliente existente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	})
	public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
		return ResponseEntity.ok(clienteService.actualizarCliente(id, cliente));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un cliente por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado")
	})
	public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
		clienteService.eliminarCliente(id);
		return ResponseEntity.noContent().build();
	}
}