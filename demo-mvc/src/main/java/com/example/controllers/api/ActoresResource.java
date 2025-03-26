package com.example.controllers.api;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.NotFoundException;
import com.example.models.entities.Actor;
import com.example.models.repositories.ActorRepository;

@RestController
@RequestMapping("/api/actores")
public class ActoresResource {
	@Autowired
	private ActorRepository dao;
	
	@GetMapping
	public List<Actor> getAll(@PageableDefault(size=20, sort = {"firstName", "lastName"})  Pageable page) {
		return dao.findAll(page).getContent();
	}
	@GetMapping(consumes = "text/plain")
	public String getAll() {
		return "Hola mundo";
	}
	@GetMapping(path="/{id:\\d+}")
	public Actor get(@PathVariable Long id) {
		Optional<Actor> item = dao.findById(id);
		if(!item.isPresent())
			throw new NotFoundException();
		return item.get();
	}
	@GetMapping(path="/{id:\\d+}/peliculas")
	public Actor getPeliculas(@PathVariable Long id) {
		Optional<Actor> item = dao.findById(id);
		if(!item.isPresent())
			throw new NotFoundException();
		return item.get();
	}
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Actor create(@RequestBody @Valid Actor item) {
		if(dao.findById(item.getActorId()).isPresent())
			throw new BadRequestException("Duplicado");
		dao.save(item);
		return item;
	}

	@PutMapping("/{id:\\d+}")
	public Actor update(@PathVariable Long id, @RequestBody @Valid Actor item) {
		if(!dao.findById(item.getActorId()).isPresent())
			throw new NotFoundException();
		dao.save(item);
		return item;
	}
	
//	@DeleteMapping("/{id:\\d+}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public void editGET(@PathVariable Long id) {
//		try {
//			dao.deleteById(id);
//		} catch (Exception e) {
//		}
//	}
}
