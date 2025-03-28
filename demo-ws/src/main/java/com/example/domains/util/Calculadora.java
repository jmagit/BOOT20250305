package com.example.domains.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculadora {
	public int suma(int a, int b) {
		return a + b;
	}
	public double suma(double a, double b) {
		return roundIEEE754(a + b);
	}
	
	public int resta(int a, int b) {
		return a - b;
	}
	public double resta(double a, double b) {
		return roundIEEE754(a - b);
	}
	
	public int multiplica(int a, int b) {
		return a * b;
	}
	public double multiplica(double a, double b) {
		return roundIEEE754(a * b);
	}
	
	public int divide(int a, int b) {
		return a / b;
	}
	
	public double divide(double a, double b) {
		if(b == 0) {
			throw new ArithmeticException("/ by zero");
		}
		return roundIEEE754(a / b);
	}
	
	private double roundIEEE754(double o) {
		return BigDecimal.valueOf(o)
				.setScale(16, RoundingMode.HALF_UP)
				.doubleValue();
	}

}
