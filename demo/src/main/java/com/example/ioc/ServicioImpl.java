package com.example.ioc;

import org.springframework.stereotype.Service;

@Service
public class ServicioImpl implements Servicio {
	private Repositorio repositorio;

	public ServicioImpl(Repositorio repositorio) {
		this.repositorio = repositorio;
	}

	@Override
	public void guardar() {
		// ...
		repositorio.guardar();
	}
}
