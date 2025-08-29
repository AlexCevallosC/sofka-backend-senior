package com.sofka.cuenta.repository;

import com.sofka.commons.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	List<Movimiento> findByNumeroCuentaAndFechaBetween(String numeroCuenta, LocalDateTime fechaInicio, LocalDateTime fechaFin);
}