package com.example.domains.contracts.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.domains.core.contracts.repositories.RepositoryWithProjections;
import com.example.domains.entities.Actor;
import com.example.domains.entities.dtos.ActorDTO;
import com.example.domains.entities.dtos.ActorShort;

@RepositoryRestResource(path = "actor", collectionResourceRel = "actores", itemResourceRel = "actor")
public interface ActorRepository extends JpaRepository<Actor, Integer> {
	@RestResource(path = "por-nombre")
	List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
	@Override
	@RestResource(exported = false)
	void deleteById(Integer id);
}
