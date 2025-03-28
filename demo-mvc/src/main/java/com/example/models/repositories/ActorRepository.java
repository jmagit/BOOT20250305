package com.example.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.example.models.entities.Actor;

@Repository
@RepositoryRestResource(path="personas")
public interface ActorRepository extends JpaRepository<Actor, Long> {

}
