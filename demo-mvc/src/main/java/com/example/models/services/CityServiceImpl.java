package com.example.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.models.entities.City;
import com.example.models.repositories.CityRepository;

@Service
public class CityServiceImpl implements CityService {
	@Autowired
	private CityRepository dao;
	
	/* (non-Javadoc)
	 * @see com.example.services.CityService#getAll()
	 */
	@Override
	public List<City> getAll() {
		return dao.findAll();
	}
	/* (non-Javadoc)
	 * @see com.example.services.CityService#getOne(long)
	 */
	@Override
	public Optional<City> getOne(long id) {
		return dao.findById(id);
	}
	/* (non-Javadoc)
	 * @see com.example.services.CityService#add(com.example.model.City)
	 */
	@Override
	public void add(City item) throws Exception {
		if(getOne(item.getCityId()).isPresent())
			throw new Exception("Clave duplicada");
		dao.save(item);
	}
	/* (non-Javadoc)
	 * @see com.example.services.CityService#modify(com.example.model.City)
	 */
	@Override
	public void modify(City item) throws Exception {
		if(!getOne(item.getCityId()).isPresent())
			throw new Exception("No existe");
		dao.save(item);
	}
	/* (non-Javadoc)
	 * @see com.example.services.CityService#delete(long)
	 */
	@Override
	public void delete(long id) throws Exception {
		if(!getOne(id).isPresent())
			return;
		dao.deleteById(id);
	}
	/* (non-Javadoc)
	 * @see com.example.services.CityService#delete(com.example.model.City)
	 */
	@Override
	public void delete(City item) throws Exception {
		delete(item.getCityId());
	}
}
