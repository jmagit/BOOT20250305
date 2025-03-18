package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Persona {
	private long id;
	private String nombre, correo, ip;
	// Ctor(), Get/Set, toString(), …
}

