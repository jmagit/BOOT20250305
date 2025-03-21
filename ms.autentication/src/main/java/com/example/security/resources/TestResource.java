package com.example.security.resources;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.dtos.AuthToken;
import com.example.security.entities.Usuario;
import com.example.security.repositories.UsuarioRepositoy;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class TestResource {
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/solo-autenticados")
	@SecurityRequirement(name = "bearerAuth")
	public String get(@Parameter(hidden = true) @RequestHeader String authorization, Principal principal) {
		var rslt = "El usuario está autenticado" + 
				"\n\tUsuario: " + principal.getName() + 
				"\n\tAuthorization: " + authorization;
		if(principal instanceof UsernamePasswordAuthenticationToken usr) {
			rslt += "\n\tAutoridades:\n\t\t" + String.join("\n\t\t", usr.getAuthorities().stream().map(e -> e.getAuthority()).toList());
		}
		return rslt;
	}
	
	@GetMapping("/solo-admin")
	@SecurityRequirement(name = "bearerAuth")
	public String getAdmin() {
		return "El usuario es administrador";
	}
	
	@Autowired
	private UsuarioRepositoy dao;
	
	@GetMapping("/solo-mio")
	@SecurityRequirement(name = "bearerAuth")
	@PostAuthorize("returnObject.IdUsuario == authentication.name")
	public Usuario getMio(@RequestParam String name) {
		try {
			return dao.findById(name).get();
		} catch (NoSuchElementException e) {
			System.err.println("No encontrado");
			throw new AccessDeniedException("No encontrado");
		}
	}
	
	@PostMapping("/solo-mio")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("#item.IdUsuario == authentication.name")
	public String postMio(@RequestBody Usuario item) {
		return "El usuario es mi usuario";
	}
	
	@GetMapping("/password/encode")
	@SecurityRequirement(name = "bearerAuth")
	public String getPass(String pass) {
		return passwordEncoder.encode(pass);
	}
	@GetMapping("/password/validate")
	@SecurityRequirement(name = "bearerAuth")
	public String getVal(String pass, String cmp) {
		return passwordEncoder.matches(pass, cmp) ? "OK":"KO";
	}
	
	@Value("${jwt.secret}")
	private String SECRET;
	
	@GetMapping("/secreto")
	@PreAuthorize("hasRole('ROLE_ADMINISTRADORES')")
	@SecurityRequirement(name = "bearerAuth")
	public String getSecreto() {
		return SECRET;
	}
	
	@GetMapping("/hmac/encode")
	@PreAuthorize("permitAll")
	@SecurityRequirement(name = "bearerAuth")
	public String generateToken() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String token = JWT.create()
				.withIssuer("MicroserviciosJWT")
				.withClaim("name", "Hola Mundo")
				.withIssuedAt(new Date(System.currentTimeMillis())).withNotBefore(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60_000))
				.sign(Algorithm.HMAC256(SECRET));
		return token;
	}
	@GetMapping("/hmac/decode")
	@PreAuthorize("permitAll")
	@SecurityRequirement(name = "bearerAuth")
	public String decodeToken(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
		DecodedJWT rslt = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer("MicroserviciosJWT").build()
				.verify(token);
		
		return rslt.getClaim("name").asString();
	}

	@ExceptionHandler({AccessDeniedException.class, IncorrectClaimException.class, JWTVerificationException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String, Object> denegar(Exception ex) {
		return Map.of(
				"status", 403,
				"type", "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3",
				"title", "Access Denied",
				"detail", ex.getMessage());
	}
}
