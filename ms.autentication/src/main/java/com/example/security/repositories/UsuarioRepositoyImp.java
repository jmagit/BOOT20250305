package com.example.security.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.security.entities.Usuario;

@Repository
public class UsuarioRepositoyImp implements UsuarioRepositoy {
	private static List<Usuario> db;
	static {
		// Contraseña: P@$$w0rd
		db = new ArrayList<>();
		db.add(new Usuario("adm@example.com", "$2a$10$HyqduzZnjWC0ittWFTNWnOaagOwLXusQelfQ4TBwjXx1bMm8.sMDe", "Administrador", List.of("ROLE_USUARIOS", "ROLE_ADMINISTRADORES")));
		db.add(new Usuario("usr@example.com", "$2a$10$wmj8GP0PlEJkuezu6zZhNu7F3O7l7G1a2nTBdy231oDUT4h67.Koq", "Usuario registrado", List.of("ROLE_USUARIOS")));
		db.add(new Usuario("emp@example.com", "$2a$10$uk8teFJl3.e8Glh2rAYMb.4H8aRD9/.xUgzeog/wvgGIT6mBGGTra", "Empleado", List.of("ROLE_USUARIOS", "ROLE_EMPLEADOS")));
		
	}
	
	@Override
	public Optional<Usuario> findById(String id) {
		return db.stream().filter(item -> item.getIdUsuario().equals(id)).findFirst();
	}

}
