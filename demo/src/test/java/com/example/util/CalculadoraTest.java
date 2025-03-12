package com.example.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.Mockito.*;

import com.example.ioc.Repositorio;
import com.example.test.utils.Smoke;

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
			// @Tag("Smoke")
			@Smoke
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
			@CsvSource({ "1,2,3", "2,-1,1", "-1,2,1", "-2,-1,-3", "0,0,0", "0.1,0.2,0.3" })
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
			@Disabled
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

	@Nested
	@DisplayName("Suplanta")
	class Suplantaciones {
		@Test
		void suplanta() {
			var calc = mock(Calculadora.class);
			when(calc.suma(anyInt(), anyInt())).thenReturn(3).thenReturn(5);

			var actual = calc.suma(2, 2);
			assertEquals(3, actual);
			assertEquals(5, calc.suma(2, 2));
			assertEquals(5, calc.suma(7,3));
		}
		@Test
		void suplanta2() {
			var calc = mock(Calculadora.class);
			when(calc.suma(anyInt(), anyInt())).thenReturn(4);
			var repo = mock(Repositorio.class);
			doNothing().when(repo).guardar();

			var obj = new Factura(calc, repo);
			var actual = obj.calcularTotal(2, 2);
			obj.emitir();
			
			assertEquals(4, actual);
			verify(calc).suma(2, 2);
			verify(repo).guardar();
		}
//		@Test
//		void Integracion() {
//			var obj = new Factura(new Calculadora());
//			var actual = obj.calcularTotal(2, 2);
//			assertEquals(4, actual);
//		}
}

}
