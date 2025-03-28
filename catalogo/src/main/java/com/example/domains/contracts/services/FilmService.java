package com.example.domains.contracts.services;

import java.util.Date;
import java.util.List;

import com.example.domains.core.contracts.services.ProjectionDomainService;
import com.example.domains.core.contracts.services.SpecificationDomainService;
import com.example.domains.entities.Film;

public interface FilmService extends ProjectionDomainService<Film, Integer>, SpecificationDomainService<Film, Integer> {
	List<Film> novedades(Date fecha);
}
