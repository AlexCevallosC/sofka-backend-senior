package com.sofka.cliente.service;

import com.sofka.commons.model.Cliente;
import java.util.List;

public interface IClienteService {
	Cliente crearCliente(Cliente cliente);
	Cliente obtenerClientePorId(Long clienteId);
	List<Cliente> obtenerTodosLosClientes();
	Cliente actualizarCliente(Long clienteId, Cliente cliente);
	void eliminarCliente(Long clienteId);
	boolean existeCliente(String identificacion);
}
