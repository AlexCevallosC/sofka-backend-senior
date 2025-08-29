package com.sofka.cliente.service;

import com.sofka.commons.model.Cliente;
import com.sofka.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClienteServiceImpl implements IClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private KafkaTemplate<String, Cliente> kafkaTemplate;

	@Override
	public Cliente crearCliente(Cliente cliente) {
		Cliente clienteGuardado = clienteRepository.save(cliente);
		return clienteGuardado;
	}

	@Override
	public Cliente obtenerClientePorId(Long clienteId) {
		return clienteRepository.findById(clienteId)
				.orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con ID: " + clienteId));
	}

	@Override
	public List<Cliente> obtenerTodosLosClientes() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente actualizarCliente(Long clienteId, Cliente cliente) {
		Cliente clienteExistente = obtenerClientePorId(clienteId);
		clienteExistente.setNombre(cliente.getNombre());
		clienteExistente.setGenero(cliente.getGenero());
		clienteExistente.setEdad(cliente.getEdad());
		clienteExistente.setDireccion(cliente.getDireccion());
		clienteExistente.setTelefono(cliente.getTelefono());
		clienteExistente.setContrasena(cliente.getContrasena());
		clienteExistente.setEstado(cliente.isEstado());

		Cliente clienteActualizado = clienteRepository.save(clienteExistente);
		return clienteActualizado;
	}

	@Override
	public void eliminarCliente(Long clienteId) {
		if (!clienteRepository.existsById(clienteId)) {
			throw new NoSuchElementException("Cliente no encontrado con ID: " + clienteId);
		}
		clienteRepository.deleteById(clienteId);
	}

	@Override
	public boolean existeCliente(String identificacion) {
		return clienteRepository.existsById(Long.valueOf(identificacion));
	}
}