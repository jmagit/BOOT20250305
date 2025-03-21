package com.example.domains.contracts.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.example.domains.entities.Language;

public interface LanguageRepository extends ListCrudRepository<Language, Integer> {
	List<Language> findAllByOrderByName();
	List<Language> findByLastUpdateGreaterThanEqualOrderByLastUpdate(Date fecha);
}
