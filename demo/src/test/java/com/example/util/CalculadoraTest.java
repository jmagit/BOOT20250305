package com.example.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CalculadoraTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("Suma dos enteros")
	void testSuma() {
		var calc = new Calculadora();
		
		var actual = calc.suma(2, 3);
		
		assertEquals(5, actual);
	}

	@Test
	@DisplayName("Suma dos reales")
	void testSuma2() {
		var calc = new Calculadora();
		
		var actual = calc.suma(0.1, 0.2);

		assertEquals(0.1, calc.suma(1, -0.9));

		assertEquals(0.3, actual);
	}

}
