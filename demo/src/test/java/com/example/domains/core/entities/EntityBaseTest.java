package com.example.domains.core.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.example.domains.core.validations.NIF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


import lombok.Value;

class EntityBaseTest {
	@Value
	class Dummy extends AbstractEntity<Dummy> {
		@Positive
		int id;
		@NotBlank
		@Size(min=2, max = 10)
		String nombre;			
	}

	@Nested
	class OK {
		@Test
		void isValid() {
			var dummy = new Dummy(1, "algo");
			assertAll("valido", 
					() -> assertTrue(dummy.isValid(), "isValid"),
					() -> assertFalse(dummy.isInvalid(), "isInvalid"),
					() -> assertEquals(0, dummy.getErrors().size(), "getErrors"),
					() -> assertNull(dummy.getErrorsFields(), "getErrorsFields"),
					() -> assertEquals("", dummy.getErrorsMessage(), "getErrorsMessage")
					);
		}
	}
	
	@Nested
	class KO {
		@Test
		void isInvalid() {
			var dummy = new Dummy(-1, " ");
			assertAll("invalido", 
					() -> assertFalse(dummy.isValid(), "isValid"),
					() -> assertTrue(dummy.isInvalid(), "isInvalid"),
					() -> assertEquals(3, dummy.getErrors().size(), "getErrors"),
					() -> assertEquals(2, dummy.getErrorsFields().size(), "getErrorsFields"),
					() -> assertEquals("ERRORES: id: debe ser mayor que 0. nombre: el tamaño debe estar entre 2 y 10, no debe estar vacío.", dummy.getErrorsMessage(), "getErrorsMessage")
					);
		}
		
	}
	

}
