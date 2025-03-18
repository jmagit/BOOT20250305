package com.example.models;

import lombok.Data;

@Data
public class PersonaDTO {
	private long id;
	private String nombre, apellidos, correo, sexo, ip;
	// Ctor(), Get/Set, toString()
}

