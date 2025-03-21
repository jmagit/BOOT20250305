package com.example.domains.contracts.repositories;

import java.util.Date;
import java.util.List;

import com.example.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.domains.entities.Actor;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.NotFoundException;


public interface ActorRepository extends ProjectionsAndSpecificationJpaRepository<Actor, Integer> {
	List<Actor> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
	
	default Actor insert(Actor item) throws DuplicateKeyException {
		if(existsById(item.getActorId()))
			throw new DuplicateKeyException();
		return save(item);
	}
	
	default Actor update(Actor item) throws NotFoundException {
		if(!existsById(item.getActorId()))
			throw new NotFoundException();
		return save(item);
	}
}
