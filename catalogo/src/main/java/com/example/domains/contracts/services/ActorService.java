package com.example.domains.contracts.services;

import java.util.Date;
import java.util.List;

import com.example.domains.core.contracts.services.ProjectionDomainService;
import com.example.domains.entities.Actor;

public interface ActorService extends ProjectionDomainService<Actor, Integer> {
	void repartePremios();
	List<Actor> novedades(Date fecha);
}
