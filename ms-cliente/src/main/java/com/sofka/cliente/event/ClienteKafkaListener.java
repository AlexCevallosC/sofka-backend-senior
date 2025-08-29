package com.sofka.cliente.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.cliente.service.IClienteService;
import com.sofka.commons.model.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class ClienteKafkaListener {

	private static final Logger LOG = LogManager.getLogger(ClienteKafkaListener.class);

	private final IClienteService clienteService;
	private final ObjectMapper objectMapper;

	@Autowired
	public ClienteKafkaListener(IClienteService clienteService, ObjectMapper objectMapper) {
		this.clienteService = clienteService;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "validar-cliente-request-topic")
	@SendTo // Sends the return value to the topic specified in the reply header
	public String handleValidationRequest(String identificacion) {
		String jsonTemplate = "{\"clienteValido\": %b}";
		boolean clienteExists = false;
		try {
			clienteExists = clienteService.existeCliente(identificacion);
		} catch (Exception e) {
			clienteExists = false;
		}
		LOG.info(identificacion.concat(" ").concat(String.format(jsonTemplate, clienteExists)));
		return String.format(jsonTemplate, clienteExists);
	}

	@KafkaListener(topics = "obtener-cliente-request-topic")
	@SendTo
	public String obtenerCliente(String identificacion) {
		try {
			Cliente cliente = clienteService.obtenerClientePorId(Long.valueOf(identificacion));
			if (cliente != null) {
				// Si el cliente se encuentra, lo serializa a JSON y lo envía de vuelta.
				LOG.info(identificacion.concat(" ").concat(objectMapper.writeValueAsString(cliente)));
				return objectMapper.writeValueAsString(cliente);
			} else {
				// Si no se encuentra, devuelve un objeto JSON vacío.
				return "{}";
			}
		} catch (JsonProcessingException e) {
			// Maneja la excepción si la serialización falla.
			LOG.error("Error serializando el cliente a JSON: {}", e.getMessage());
			return "{}";
		}
	}
}