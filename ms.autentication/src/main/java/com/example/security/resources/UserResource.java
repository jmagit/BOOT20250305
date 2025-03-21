package com.example.security.resources;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.security.dtos.AuthToken;
import com.example.security.dtos.BasicCredential;
import com.example.security.dtos.RefreshToken;
import com.example.security.entities.Usuario;
import com.example.security.repositories.UsuarioRepositoy;

@RestController
//	@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true", methods={RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS })
//	@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials="false")
public class UserResource {
	@Value("${jwt.key.private}")
	private String privateKeyContent;
	@Value("${jwt.key.public}")
	private String publicKeyContent;
	@Value("${jwt.secret}")
	private String secretKey;
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	@Value("${jwt.expiracion.mim:10}")
	private int EXPIRES_IN_MINUTES;
	@Value("${jwt.refresh.factor:4}")
	private int REFRESH_FACTOR;
	@Value("${jwt.refresh.not-before:0}")
	private int REFRESH_NOT_BEFORE;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioRepositoy dao;
	
	// { "username": "adm@example.com", "password": "P@$$w0rd" }

	@PostConstruct
	private void inicializa() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        privateKey = (RSAPrivateKey)kf.generatePrivate(keySpecPKCS8);
        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
	}
	
	@PostMapping(path = "/login", consumes = "application/json")
	public AuthToken loginJSON(@Valid @RequestBody BasicCredential credential)  {
		var item = dao.findById(credential.getUsername());
		if (item.isEmpty() || !item.get().isActive() || !passwordEncoder.matches(credential.getPassword(), item.get().getPassword()))
			return new AuthToken();
		return getAuthToken(item.get());
	}

	@PostMapping(path = "/login/refresh", consumes = "application/json")
	public AuthToken refresh(@Valid @RequestBody RefreshToken token)  {
		// Se debería comprobar que no ha sido usado previamente
		DecodedJWT decode = JWT.require(refreshAlgorithm()).withAudience("refresh").build()
				.verify(token.getToken());
		var item = dao.findById(decode.getClaim("username").asString());
		if (item.isEmpty() || !item.get().isActive())
			return new AuthToken();
		return getAuthToken(item.get());
	}

	@ExceptionHandler({IncorrectClaimException.class, JWTVerificationException.class, MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String, Object> denegar(Exception ex) {
		return Map.of(
				"status", 403,
				"type", "https://datatracker.ietf.org/doc/html/rfc7231#section-6.5.3",
				"title", "Access Denied",
				"detail", ex.getMessage());
	}
	
	private AuthToken getAuthToken(Usuario usr) {
		return new AuthToken(true, "Bearer " + getToken(usr), getRefreshToken(usr), usr.getNombre(), 
				usr.getRoles().stream().map(s->s.substring(5,6) + s.substring(6).toLowerCase()).toList(), EXPIRES_IN_MINUTES * 60);
	}

	private String getToken(Usuario usr) {
		String token = JWT.create()
				.withIssuer("MicroserviciosJWT")
				.withAudience("authorization")
				.withClaim("username", usr.getIdUsuario())
				.withArrayClaim("authorities", usr.getRoles().toArray(new String[0]))
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES_IN_MINUTES * 60_000))
				.sign(Algorithm.RSA256(publicKey, privateKey));
		return token;
	}

	private String getRefreshToken(Usuario usr) {
		String token = JWT.create()
				.withIssuer("MicroserviciosJWT")
				.withAudience("refresh")
				.withClaim("username", usr.getIdUsuario())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withNotBefore(new Date(System.currentTimeMillis() - 100 + REFRESH_NOT_BEFORE * 60_000))
				.withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_FACTOR * EXPIRES_IN_MINUTES * 60_000))
				.sign(refreshAlgorithm());
		return token;
	}

	private Algorithm refreshAlgorithm() {
		return Algorithm.HMAC256(secretKey);
//		return Algorithm.RSA256(publicKey, privateKey);
	}

	/*
	 * /register (anonimo) 
	 * /changepassword 
	 * /profile (Authorization) (get, put) menos la contraseña 
	 * /users (Admin) (get, post, put, delete) + roles menos la contraseña
	 *
	 */
}
