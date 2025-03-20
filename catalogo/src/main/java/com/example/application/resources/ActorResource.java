package com.example.application.resources;

import java.net.URI;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.domains.contracts.services.ActorService;
import com.example.domains.entities.models.ActorDTO;
import com.example.domains.entities.models.ActorShort;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/actores")
public class ActorResource {
	private ActorService srv;

	public ActorResource(ActorService srv) {
		this.srv = srv;
	}
	
	@GetMapping(path = "/v1")
	public List<?> getAll(@RequestParam(required = false, defaultValue = "largo") String modo) {
		if("short".equals(modo))
			return (List<?>) srv.getByProjection(Sort.by("firstName", "lastName"), ActorShort.class);
		else
			return (List<?>) srv.getByProjection(Sort.by("firstName", "lastName"), ActorDTO.class); // srv.getAll();;
	}
	
	@GetMapping(path = "/v2")
	public List<ActorDTO> getAllv2(@RequestParam(required = false) String sort) {
		if (sort != null)
			return (List<ActorDTO>) srv.getByProjection(Sort.by(sort), ActorDTO.class);
		return srv.getByProjection(ActorDTO.class);
	}
	
	@GetMapping(path = { "/v1", "/v2" }, params = "page")
	public PagedModel<ActorShort> getAll(@ParameterObject Pageable page) {
		return new PagedModel<ActorShort>(srv.getByProjection(page, ActorShort.class));
	}
	
	@GetMapping(path = { "/v1/{id}", "/v2/{id}" })
	public ActorDTO getOne(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if(item.isEmpty())
			throw new NotFoundException();
		return ActorDTO.from(item.get());
	}
	
	record Peli(int id, String titulo) {}
	
	@GetMapping(path = {"/v1/{id}/pelis","/v2/{id}/pelis"})
	@Transactional
	public List<Peli> getPelis(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if(item.isEmpty())
			throw new NotFoundException();
		return item.get().getFilmActors().stream()
				.map(o -> new Peli(o.getFilm().getFilmId(), o.getFilm().getTitle()))
				.toList();
	}
	
	@DeleteMapping(path = "/v1/{id}/jubilacion")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void jubilar(@PathVariable int id) throws NotFoundException {
		var item = srv.getOne(id);
		if(item.isEmpty())
			throw new NotFoundException();
		item.get().jubilate();
	}
	
	@PostMapping(path = { "/v1", "/v2" })
	public ResponseEntity<Object> create(@Valid @RequestBody ActorDTO item) throws BadRequestException, DuplicateKeyException, InvalidDataException {
		var newItem = srv.add(ActorDTO.from(item));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(newItem.getActorId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping(path = { "/v1/{id}", "/v2/{id}" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@PathVariable int id, @Valid @RequestBody ActorDTO item) throws NotFoundException, InvalidDataException, BadRequestException {
		if(id != item.getActorId())
			throw new BadRequestException("No coinciden los identificadores");
		srv.modify(ActorDTO.from(item));
	}

	@DeleteMapping(path = { "/v1/{id}", "/v2/{id}" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable int id) {
		srv.deleteById(id);
	}
}
