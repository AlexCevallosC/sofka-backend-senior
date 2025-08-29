package com.sofka.cuenta.repository;

import com.sofka.commons.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, String> {
	List<Cuenta> findByClienteIdentificacion(String clienteIdentificacion);
}