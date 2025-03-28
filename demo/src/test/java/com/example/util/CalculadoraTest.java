package com.example.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculadoraTest {
	Calculadora calc;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		calc = new Calculadora();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Nested
	@DisplayName("Método: suma")
	class Suma {
		@Nested
		@DisplayName("Casos validos")
		class OK {
			@Test
			@DisplayName("Suma dos enteros")
			void testSuma() {
//		var calc = new Calculadora();

				var actual = calc.suma(2, 3);

				assertEquals(5, actual);
			}

			@Test
			@DisplayName("Suma dos reales")
			void testSuma2() {
				var calc = new Calculadora();

				var actual = calc.suma(0.1, 0.2);

				assertEquals(0.3, actual);
			}

			@Test
			@DisplayName("Suma dos reales: resta")
			void testSuma3() {
				var calc = new Calculadora();

				assertEquals(0.1, calc.suma(1, -0.9));
			}
			
			@DisplayName("Suma dos numeros")
			@ParameterizedTest(name = "{0} + {1} = {2}")
			@CsvSource({ "1,2,3", "2,-1,1", "-1,2,1","-2,-1,-3","0,0,0","0.1,0.2,0.3" })
			void testSumaParametrizada(double operando1, double operando2, double esperado) {
				var calc = new Calculadora();

				var actual = calc.suma(operando1, operando2);

				assertEquals(esperado, actual);
			}

		}

		@Nested
		@DisplayName("Casos invalidos")
		class KO {
			@Test
			@DisplayName("Suma dos enteros grandes")
			void testSumaInt() {
				var calc = new Calculadora();

				var actual = calc.suma(Integer.MAX_VALUE, 1);
				assertEquals(5, actual);

				assertTrue(1.0 / 0 > 0);
				assertEquals(5, 1.0 / 0);
//		assertTrue(actual > 0);
			}
		}

	}

	@Nested
	@DisplayName("Método: divide")
	class Divide {
		@Nested
		@DisplayName("Casos validos")
		class OK {
			@Test
			@DisplayName("Divide dos enteros")
			void testDivide() {
				var calc = new Calculadora();

				assertEquals(0.5, calc.divide(1.0, 2));
			}

			@Test
			@DisplayName("Divide por cero")
			void testDivide2() {
				var calc = new Calculadora();
				var ex = assertThrows(ArithmeticException.class, () -> calc.divide(1, 0));
				assertEquals("/ by zero", ex.getMessage());
			}

			@Test
			@DisplayName("Divide por cero: try")
			void testDivide3() {
				var calc = new Calculadora();
				try {
					calc.divide(1, 0);
					fail("No se ha lanzado excepcion");
				} catch (ArithmeticException ex) {
					assertEquals("/ by zero", ex.getMessage());
				}
			}
		}
	}
}
