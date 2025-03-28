package com.example.application.contracts;

import java.util.Date;

import com.example.application.models.NovedadesDTO;


public interface CatalogoService {

	NovedadesDTO novedades(Date fecha);

}