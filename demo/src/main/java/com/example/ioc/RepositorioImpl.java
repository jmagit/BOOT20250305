package com.example.ioc;

import org.springframework.stereotype.Repository;

@Repository
public class RepositorioImpl implements Repositorio {
	public RepositorioImpl(Configuracion config) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void guardar() {
		System.err.println("Guardando");
	}
}
