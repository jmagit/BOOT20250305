package com.example.ioc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

//@Repository
//@Qualifier("verdad")
public class RepositorioImpl implements Repositorio {
	public RepositorioImpl(Configuracion config, Registro registro) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void guardar() {
		System.err.println("Guardando");
	}
}
