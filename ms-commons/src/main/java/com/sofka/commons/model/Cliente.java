package com.sofka.commons.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "identificacion")
public class Cliente extends Persona {
	private String contrasena;
	private boolean estado;
}