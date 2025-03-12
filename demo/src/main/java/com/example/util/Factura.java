package com.example.util;

import com.example.ioc.Repositorio;

public class Factura {
	Calculadora calculadora;
	Repositorio repositorio;
	
	public Factura(Calculadora calculadora, Repositorio repositorio) {
		super();
		this.calculadora = calculadora;
		this.repositorio = repositorio;
	}
	
	public double calcularTotal(int cantidad, int precio) {
		return calculadora.suma(cantidad, precio);
	}
	
	public void emitir() {
		//...
		repositorio.guardar();
	}
}
