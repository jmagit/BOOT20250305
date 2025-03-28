package com.example.domains.contracts.services;

import java.util.Date;
import java.util.List;

import com.example.domains.core.contracts.services.DomainService;
import com.example.domains.entities.Language;

public interface LanguageService extends DomainService<Language, Integer> {
	List<Language> novedades(Date fecha);
}
