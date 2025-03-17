package com.example.domains.core.contracts.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationDomainService<E, K> extends PagingAndSortingDomainService<E, K> {
	Optional<E> getOne(Specification<E> spec);
	List<E> getAll(Specification<E> spec);
	Page<E> getAll(Specification<E> spec, Pageable pageable);
	List<E> getAll(Specification<E> spec, Sort sort);
}
