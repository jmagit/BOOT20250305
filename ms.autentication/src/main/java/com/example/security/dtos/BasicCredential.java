package com.example.security.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "Credenciales")
public class BasicCredential {
	@NotBlank
	@Email
	@Schema(example = "adm@example.com")
	private String username;
	@NotBlank
	@Pattern(
			regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$",
			message="debe contener al menos 8 caracteres con letras mayúsculas, minúsculas, números y símbolos"
		)
	@Schema(example = "P@$$w0rd")
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
