package com.example.domains.entities;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ActorTest {

	@Test
	@DisplayName("Es un actor valido")
	void testIsValid() {
		var fixture = new Actor(0, "PEPITO", "GRILLO");
		assertTrue(fixture.isValid());
	}

	@DisplayName("El nombre de tener entre 2 y 45 caracteres, y no puede estar en blanco")
	@ParameterizedTest(name = "nombre: -{0}- -> {1}")
	@CsvSource(value = { "'','ERRORES: firstName: el tamaño debe estar entre 2 y 45, no debe estar vacío.'",
			"' ','ERRORES: firstName: el tamaño debe estar entre 2 y 45, no debe estar vacío.'",
			"'   ','ERRORES: firstName: no debe estar vacío.'",
			"A,'ERRORES: firstName: el tamaño debe estar entre 2 y 45.'",
			"12345678901234567890123456789012345678901234567890,'ERRORES: firstName: el tamaño debe estar entre 2 y 45.'" })
	void testNombreIsInvalid(String valor, String error) {
		var fixture = new Actor(0, valor, "GRILLO");
//		assertTrue(fixture.isInvalid());
		assertEquals(error, fixture.getErrorsMessage());
	}
	
	@DisplayName("Apellidos de tener entre 2 y 45 caracteres, y no puede estar en blanco")
	@ParameterizedTest(name = "apellidos: -{0}-")
	@ValueSource(strings = { " ", "    ", "p", "12345678901234567890123456789012345678901234567890"})
	@NullAndEmptySource
	void testApellidosIsInvalid(String valor) {
		var fixture = new Actor(0, "GRILLO", valor);
		assertTrue(fixture.isInvalid());
	}

	@Test
	@DisplayName("Solo compara la PK")
	void testPrimaryKeyOK() {
		var fixture1 = new Actor(666, "Pepito", "GRILLO");
		var fixture2 = new Actor(666, "GRILLO", "Pepito");
		assertAll("PK", 
				() -> assertTrue(fixture1.equals(fixture2)),
				() -> assertTrue(fixture2.equals(fixture1)),
				() -> assertTrue(fixture1.hashCode() == fixture2.hashCode())
				);
	}

	@Test
	@DisplayName("Solo la PK diferente")
	void testPrimaryKeyKO() {
		var fixture1 = new Actor(666, "Pepito", "GRILLO");
		var fixture2 = new Actor(665, "Pepito", "GRILLO");
		assertAll("PK", 
				() -> assertFalse(fixture1.equals(fixture2)),
				() -> assertFalse(fixture2.equals(fixture1)),
				() -> assertTrue(fixture1.hashCode() != fixture2.hashCode())
				);
	}

}
