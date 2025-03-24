package com.example.domains.contracts.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.domains.core.contracts.repositories.RepositoryWithProjections;
import com.example.domains.entities.Actor;
import com.example.domains.entities.dtos.ActorDTO;
import com.example.domains.entities.dtos.ActorShort;

@RepositoryRestResource(exported = false)
public interface ActoresRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor>, RepositoryWithProjections {
	List<Actor> findTop5ByFirstNameStartingWithOrderByLastNameDesc(String prefijo);
	List<Actor> findTop5ByFirstNameStartingWith(String prefijo, Sort orderBy);
	
	List<Actor> findByActorIdGreaterThan(int id);
	@Query(value = "SELECT a FROM Actor a WHERE a.actorId > ?1")
	List<Actor> findNovedadesJPQL(int id);
	@Query(value = "SELECT * FROM actor a WHERE a.actor_id > :id", nativeQuery = true)
	List<Actor> findNovedadesSQL(int id);
	
	List<ActorDTO> queryByActorIdGreaterThan(int id);
	List<ActorShort> getByActorIdGreaterThan(int id);

	<T> List<T> findByActorIdGreaterThan(int id, Class<T> type);

}
