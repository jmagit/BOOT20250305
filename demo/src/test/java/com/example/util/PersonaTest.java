package com.example.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PersonaTest {
	@Test
	@Tag("smoke")
	void createPersona() {
		var p = new Persona(1, "Pepe");
		
		assertNotNull(p);
		assertAll("Contructor", 
				() -> assertEquals(1, p.id),
				() -> assertEquals("Pepe", p.nombre, "nombre"),
				() -> assertEquals("Pepe", p.apellidos, "apellidos")
				);
		assertEquals(1, p.id);
		assertEquals("Pepe", p.nombre);
	}
}
