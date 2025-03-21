package com.example.application.services;

import java.util.Date;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.contracts.CatalogoService;
import com.example.application.models.NovedadesDTO;
import com.example.domains.contracts.services.ActorService;
import com.example.domains.contracts.services.CategoryService;
import com.example.domains.contracts.services.FilmService;
import com.example.domains.contracts.services.LanguageService;
import com.example.domains.entities.models.ActorDTO;
import com.example.domains.entities.models.FilmShortDTO;

@Service
public class CatalogoServiceImpl implements CatalogoService {
	@Autowired
	private FilmService filmSrv;
	@Autowired
	private ActorService artorSrv;
	@Autowired
	private CategoryService categorySrv;
	@Autowired
	private LanguageService languageSrv;

	@Override
	public NovedadesDTO novedades(Date fecha) {
		// Date fecha = Date.valueOf("2019-01-01 00:00:00");
		if(fecha == null)
			fecha = Date.from(Instant.now().minusSeconds(36000));
		return new NovedadesDTO(
				filmSrv.novedades(fecha).stream().map(item -> new FilmShortDTO(item.getFilmId(), item.getTitle())).toList(), 
				artorSrv.novedades(fecha).stream().map(item -> ActorDTO.from(item)).toList(), 
				categorySrv.novedades(fecha), 
				languageSrv.novedades(fecha)
				);
	}

}
