package com.example.ioc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Dependencias {
	@Bean
	Repositorio repositorio() {
		return new RepositorioMock();
	}
}
