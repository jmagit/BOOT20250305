package com.example.domains.contracts.services;

import com.example.domains.core.contracts.services.DomainService;
import com.example.domains.entities.Actor;

public interface ActoresService extends DomainService<Actor, Integer> {
	void repartePremios();
}
