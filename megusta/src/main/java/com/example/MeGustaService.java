package com.example;

import java.util.Date;
import java.util.Random;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/me-gusta")
public class MeGustaService {
	public final String ME_GUSTA_CONT = "megusta";
	public final String ME_GUSTA_PELIS = "pelis";
	@Autowired
	private StringRedisTemplate template;

	private ValueOperations<String, String> redisValue;
	private HashOperations<String, String, String> hashOperations;
	
	@PostConstruct
	private void inicializa() {
		redisValue = template.opsForValue();
		if (redisValue.get(ME_GUSTA_CONT) == null)
			redisValue.set(ME_GUSTA_CONT, "0");
		hashOperations = template.opsForHash();
	}

	@GetMapping("/cont")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente")
	private String get() {
		return "Llevas " + redisValue.get(ME_GUSTA_CONT);
	}

	@PostMapping("/cont")
	@Operation(summary = "Manda un Me Gusta")
	@SecurityRequirement(name = "bearerAuth")
	private String add() {
		return "Llevas " + redisValue.increment(ME_GUSTA_CONT);
	}

	@PutMapping("/cont/{miles}")
	@Operation(summary = "Manda miles de Me Gusta")
	private String add(@Parameter(description = "Número de miles a enviar") @PathVariable int miles) {
		long r = 0;
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; r = redisValue.increment(ME_GUSTA_CONT))
			;
		return "Llevas " + r + ". Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}

	@GetMapping("/hash/{id}")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente una pelicula")
	private String getPeli(@PathVariable int id) {
		var count = hashOperations.get(ME_GUSTA_PELIS, Integer.toString(id));
		return "Llevas " + (count == null ? "0" : count) + " likes";
	}

	@PostMapping("/hash/{id}")
	@Operation(summary = "Manda un Me Gusta a una pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addPeli(@PathVariable int id) {
		return "Llevas " + hashOperations.increment(ME_GUSTA_PELIS, Integer.toString(id), 1);
	}

	@PutMapping("/hash/{miles}")
	@Operation(summary = "Manda miles de Me Gusta a las pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addPeliMiles(@PathVariable int miles) {
		Random rnd = new Random();
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; hashOperations.increment(ME_GUSTA_PELIS, Integer.toString(rnd.nextInt(1, 1001)), 1))
			;
		return "Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}
	
	@Autowired
	PeliculaContRepository dao;
	
	@GetMapping("/repo/{id}")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente una pelicula")
	private String getRepo(@PathVariable int id) {
		var item = dao.findById(id);
		return "Llevas " + (item.isEmpty() ? "0" : item.get().getCont()) + " likes";
	}

	@PostMapping("/repo/{id}")
	@Operation(summary = "Manda un Me Gusta a una pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addRepo(@PathVariable int id) {
		var item = dao.findById(id);
		PeliculaCont peli;
		if(item.isEmpty()) {
			peli =new PeliculaCont(id, 1);
		} else {
			peli = item.get();
			peli.addCont();
		}
		dao.save(peli);
		return "Llevas " + peli.getCont();
	}

	@PutMapping("/repo/{miles}")
	@Operation(summary = "Manda miles de Me Gusta a las pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addRepoMiles(@PathVariable int miles) {
		Random rnd = new Random();
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; addRepo(rnd.nextInt(1, 101)));
		return "Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}

}
