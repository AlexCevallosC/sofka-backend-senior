package com.sofka.cuenta.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String nombre;
    private boolean estado;
}
