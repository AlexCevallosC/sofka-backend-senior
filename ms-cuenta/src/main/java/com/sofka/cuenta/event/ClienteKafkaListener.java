package com.sofka.cuenta.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteKafkaListener {



    //private final CuentaRepository cuentaRepository;

	/*
    @KafkaListener(topics = "cliente-topic", containerFactory = "kafkaListenerContainerFactory")
    public void handleClienteCreado(ClienteDTO cliente) {
        log.info("Evento de cliente recibido: {}", cliente.getId());
        // Aquí se podría implementar lógica adicional, como validar que el cliente exista
        // antes de crear una cuenta, o simplemente registrar el evento.
    }
    */

    /*
	@KafkaListener(topics = "cliente-eliminados-topic")
    @Transactional
    public void handleClienteEliminado(Long clienteId) {
        log.warn("Evento de cliente eliminado recibido: {}. Desactivando cuentas asociadas.", clienteId);
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        cuentas.forEach(cuenta -> {
            cuenta.setEstado(false);
            cuentaRepository.save(cuenta);
        });
        log.info("Cuentas del cliente {} desactivadas.", clienteId);
    }
    */
}
