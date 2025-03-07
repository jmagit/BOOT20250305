package com.example.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class Registro {
	@Autowired
	private Configuracion config;
	
	public Registro() {
		System.err.println("Registro creado");
	}
	
	@PostConstruct
	private void init() {
		config.init();
	}
}
