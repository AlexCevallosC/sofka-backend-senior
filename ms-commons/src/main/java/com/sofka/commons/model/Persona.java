package com.sofka.commons.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {
	@Id
	private String identificacion; // Se usará como PK
	private String nombre;
	private String genero;
	private int edad;
	private String direccion;
	private String telefono;
}