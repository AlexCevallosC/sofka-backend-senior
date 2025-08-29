package com.sofka.cuenta.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.commons.model.Cliente;
import com.sofka.commons.model.Cuenta;
import com.sofka.commons.model.Movimiento;
import com.sofka.cuenta.event.ClienteKafkaListener;
import com.sofka.cuenta.repository.CuentaRepository;
import com.sofka.cuenta.repository.MovimientoRepository;
import com.sofka.cuenta.service.dto.ReporteDTO;
import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImpl implements ICuentaService {

	private static final Logger LOG = LogManager.getLogger(ClienteKafkaListener.class);

	@Autowired
	private CuentaRepository cuentaRepository;

	@Autowired
	private MovimientoRepository movimientoRepository;

	@Autowired
	private ReplyingKafkaTemplate<String, String, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	// F1 - CRUD de Cuenta
	@Override
	public Cuenta crearCuenta(Cuenta cuenta) {
		if (!validarClienteAsincronicamente(cuenta.getClienteIdentificacion())) {
			throw new IllegalArgumentException("El cliente con la identificación ingresada no existe..");
		}
		return cuentaRepository.save(cuenta);
	}

	@Override
	public Cuenta obtenerCuentaPorNumero(String numeroCuenta) {
		return cuentaRepository.findById(numeroCuenta)
				.orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada con número: " + numeroCuenta));
	}

	@Override
	public List<Cuenta> obtenerTodasLasCuentas() {
		return cuentaRepository.findAll();
	}

	@Override
	public Cuenta actualizarCuenta(String numeroCuenta, Cuenta cuenta) {
		Cuenta cuentaExistente = obtenerCuentaPorNumero(numeroCuenta);
		cuentaExistente.setTipoCuenta(cuenta.getTipoCuenta());
		cuentaExistente.setSaldoInicial(cuenta.getSaldoInicial());
		cuentaExistente.setEstado(cuenta.isEstado());
		return cuentaRepository.save(cuentaExistente);
	}

	@Override
	public void eliminarCuenta(String numeroCuenta) {
		if (!cuentaRepository.existsById(numeroCuenta)) {
			throw new NoSuchElementException("Cuenta no encontrada con número: " + numeroCuenta);
		}
		cuentaRepository.deleteById(numeroCuenta);
	}

	// F2 y F3 - Registro de Movimientos
	@Override
	public Movimiento registrarMovimiento(Movimiento movimiento) {
		Cuenta cuenta = obtenerCuentaPorNumero(movimiento.getNumeroCuenta());
		BigDecimal saldoActual = cuenta.getSaldoInicial();

		if ("Retiro".equals(movimiento.getTipoMovimiento())) {
			if (saldoActual.compareTo(movimiento.getValor()) < 0) {
				throw new IllegalStateException("Saldo no disponible");
			}
			saldoActual = saldoActual.subtract(movimiento.getValor());
		} else if ("Depósito".equals(movimiento.getTipoMovimiento())) {
			saldoActual = saldoActual.add(movimiento.getValor());
		} else {
			throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimiento.getTipoMovimiento());
		}

		cuenta.setSaldoInicial(saldoActual);
		movimiento.setFecha(LocalDateTime.now());
		movimiento.setSaldo(saldoActual);

		cuentaRepository.save(cuenta);
		return movimientoRepository.save(movimiento);
	}

	// F4 - Reportes
	@Override
	public ReporteDTO generarReporte(String clienteId, String fechaInicio, String fechaFin) {

		Cliente cliente = obtenerClienteAsincronicamente(clienteId)
				.orElseThrow(() ->
						new IllegalArgumentException("El cliente con la identificación " + clienteId + " no existe."));

		List<Cuenta> cuentas = obtenerCuentasPorIdentificacion(clienteId);

		LocalDateTime inicio = LocalDateTime.parse(fechaInicio, DateTimeFormatter.ofPattern("yyyy-MM-dd"+"00:00:00"));
		LocalDateTime fin = LocalDateTime.parse(fechaFin, DateTimeFormatter.ofPattern("yyyy-MM-dd"+"23:59:59"));

		ReporteDTO reporte = new ReporteDTO();

		reporte.setFecha(LocalDateTime.now());
		reporte.setClienteNombre(cliente.getNombre());

		List<ReporteDTO.CuentaReporte> cuentaReportes = new ArrayList<>();
		for (Cuenta cuenta : cuentas) {
			List<Movimiento> movimientos = movimientoRepository.findByNumeroCuentaAndFechaBetween(
					cuenta.getNumeroCuenta(),
					inicio,
					fin
			);

			/*
			// Mapear la lista de Movimientos a una lista de MovimientoReporte
			List<ReporteDTO.CuentaReporte> movimientoReportes = movimientos.stream()
					.map(movimiento -> {
						ReporteDTO.CuentaReporte rc = new ReporteDTO.CuentaReporte();
						rc.setNumeroCuenta(cuenta.getNumeroCuenta());
						rc.setTipoCuenta(cuenta.getTipoCuenta());
						rc.setSaldoInicial(cuenta.getSaldoInicial());
						rc.setEstado(cuenta.isEstado());
						return rc;
					})
					.collect(Collectors.toList());
			*/
			// Crear y llenar el objeto CuentaReporte
			ReporteDTO.CuentaReporte cr = new ReporteDTO.CuentaReporte();
			cr.setNumeroCuenta(cuenta.getNumeroCuenta());
			cr.setTipoCuenta(cuenta.getTipoCuenta());
			cr.setSaldoInicial(cuenta.getSaldoInicial());
			cr.setEstado(cuenta.isEstado());
			cr.setMovimientos(movimientos);

			cuentaReportes.add(cr);
		}

		return reporte;
	}

	@Override
	public List<Movimiento> obtenerMovimientosPorNumeroYrangoFechas(
			String numeroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
		return
				movimientoRepository.findByNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicio, fechaFin);
	}

	public List<Cuenta> obtenerCuentasPorIdentificacion(String clienteIdentificacion) {
		List<Cuenta> cuentas = cuentaRepository.findByClienteIdentificacion(clienteIdentificacion);
		if (cuentas.isEmpty()) {
			throw new EntityNotFoundException("No se encontraron cuentas para el cliente con identificación: " + clienteIdentificacion);
		}
		return cuentas;
	}

	private boolean validarClienteAsincronicamente(String clienteIdentificacion) {
		String requestTopic = "validar-cliente-request-topic";
		String replyTopic = "validar-cliente-reply-topic";

		try {
			Message<String> message = MessageBuilder.withPayload(clienteIdentificacion)
					.setHeader(KafkaHeaders.TOPIC, requestTopic)
					.setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
					.build();

			RequestReplyMessageFuture<String, String> requestReplyFuture = kafkaTemplate.sendAndReceive(message);

			Message<?> replyMessage = requestReplyFuture.get(10, TimeUnit.SECONDS);

			if (replyMessage == null) {
				LOG.error("Mensaje de respuesta nulo. No se recibió respuesta de ms-cliente.");
				return false;
			}

			String stringPayload = (String) replyMessage.getPayload();
			String replyPayload = stringPayload
					.replace("\\", "")
					.replace("\"{", "{")
					.replace("}\"", "}")
					.replace("\\", "")
					.trim();

			if (replyPayload == null) {
				LOG.error("El payload de la respuesta es nulo.");
				return false;
			}

			LOG.info(clienteIdentificacion.concat(" replyPayload -> ").concat(replyPayload));
			JsonNode rootNode = objectMapper.readTree(replyPayload);
			JsonNode clienteValidoNode = rootNode.get("clienteValido");

			if (clienteValidoNode == null) {
				LOG.error("La clave 'clienteValido' no se encontró en la respuesta JSON.");
				return false;
			}

			LOG.info(clienteIdentificacion.concat(" clienteValido -> ").concat(clienteValidoNode.asText()));
			return clienteValidoNode.asBoolean();

		} catch (InterruptedException | ExecutionException | JsonProcessingException | TimeoutException e) {
			System.err.println("Error durante la validación del cliente vía Kafka: " + e.getMessage());
			return false;
		}
	}

	public Optional<Cliente> obtenerClienteAsincronicamente(String clienteIdentificacion) {
		String requestTopic = "obtener-cliente-request-topic";
		String replyTopic = "obtener-cliente-reply-topic";

		try {
			Message<String> message = MessageBuilder.withPayload(clienteIdentificacion)
					.setHeader(KafkaHeaders.TOPIC, requestTopic)
					.setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
					.build();

			RequestReplyMessageFuture<String, String> requestReplyFuture = kafkaTemplate.sendAndReceive(message);

			Message<?> replyMessage = requestReplyFuture.get(10, TimeUnit.SECONDS);

			if (replyMessage == null) {
				LOG.error("Mensaje de respuesta nulo. No se recibió respuesta de ms-cliente.");
				return Optional.empty();
			}

			String replyPayload = (String) replyMessage.getPayload();

			// Handling special case for String-wrapped JSON (sometimes from Kafka)
			String cleanedPayload = replyPayload.replace("\\\"", "\"");
			if (cleanedPayload.startsWith("\"") && cleanedPayload.endsWith("\"")) {
				cleanedPayload = cleanedPayload.substring(1, cleanedPayload.length() - 1);
			}

			Cliente cliente = objectMapper.readValue(cleanedPayload, Cliente.class);

			if (cliente != null && cliente.getIdentificacion() != null) {
				LOG.info("Cliente {} encontrado: {}", cliente.getIdentificacion(), cliente);
				return Optional.of(cliente);
			} else {
				LOG.warn("Cliente con identificación {} no encontrado en el servicio de clientes.", clienteIdentificacion);
				return Optional.empty();
			}

		} catch (InterruptedException | ExecutionException | JsonProcessingException | TimeoutException e) {
			LOG.error("Error durante la obtención del cliente vía Kafka: {}", e.getMessage(), e);
			return Optional.empty();
		}
	}

}