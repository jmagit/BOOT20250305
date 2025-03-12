package com.example.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Dependencias {
	@Bean
	Repositorio repositorio(Configuracion config, Registro registro) {
//		System.err.println("soy el bean");
		//return new RepositorioMock();
		return new RepositorioImpl(config, registro);
	}
	@Bean
	Repositorio repo1(Configuracion config, Registro registro) {
		return new RepositorioImpl(config, registro);
	}
	@Bean
	Repositorio repo2() {
		return new RepositorioMock();
	}
}
