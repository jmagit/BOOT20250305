package com.example.security.repositories;

import java.util.Optional;

import com.example.security.entities.Usuario;

public interface UsuarioRepositoy {
	Optional<Usuario> findById(String id);
}
