package com.example.domains.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.domains.contracts.repositories.FilmRepository;
import com.example.domains.contracts.services.FilmService;
import com.example.domains.entities.Film;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.NonNull;

@Service
public class FilmServiceImpl implements FilmService {
	private FilmRepository dao;

	public FilmServiceImpl(FilmRepository dao) {
		this.dao = dao;
	}

	@Override
	public <T> List<T> getByProjection(@NonNull Class<T> type) {
		return dao.findAllBy(type);
	}

	@Override
	public <T> List<T> getByProjection(@NonNull Sort sort, @NonNull Class<T> type) {
		return dao.findAllBy(sort, type);
	}

	@Override
	public <T> Page<T> getByProjection(@NonNull Pageable pageable, @NonNull Class<T> type) {
		return dao.findAllBy(pageable, type);
	}

	@Override
	public List<Film> getAll(@NonNull Sort sort) {
		return dao.findAll(sort);
	}

	@Override
	public Page<Film> getAll(@NonNull Pageable pageable) {
		return dao.findAll(pageable);
	}

	@Override
	public List<Film> getAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Film> getOne(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Optional<Film> getOne(@NonNull Specification<Film> spec) {
		return dao.findOne(spec);
	}

	@Override
	public List<Film> getAll(@NonNull Specification<Film> spec) {
		return dao.findAll(spec);
	}

	@Override
	public Page<Film> getAll(@NonNull Specification<Film> spec, @NonNull Pageable pageable) {
		return dao.findAll(spec, pageable);
	}

	@Override
	public List<Film> getAll(@NonNull Specification<Film> spec, @NonNull Sort sort) {
		return dao.findAll(spec, sort);
	}

	@Override
	@Transactional
	public Film add(Film item) throws DuplicateKeyException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		if(dao.existsById(item.getFilmId()))
			throw new DuplicateKeyException(item.getErrorsMessage());
		return dao.save(item);
	}

	@Override
	@Transactional
	public Film modify(Film item) throws NotFoundException, InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		if(item.isInvalid())
			throw new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields());
		var leido = dao.findById(item.getFilmId()).orElseThrow(() -> new NotFoundException());
		return dao.save(item.merge(leido));
	}

	@Override
	public void delete(Film item) throws InvalidDataException {
		if(item == null)
			throw new InvalidDataException("No puede ser nulo");
		deleteById(item.getFilmId());
	}

	@Override
	public void deleteById(Integer id) {
		dao.deleteById(id);
	}

	@Override
	public List<Film> novedades(@NonNull Date fecha) {
		return dao.findByLastUpdateGreaterThanEqualOrderByLastUpdate(fecha);
	}

}
