package com.example.ioc;

import org.springframework.stereotype.Component;

@Component
public class Registro {
	public Registro() {
		System.err.println("Registro creado");
	}
}
