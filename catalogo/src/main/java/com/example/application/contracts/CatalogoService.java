package com.example.application.contracts;

import java.sql.Timestamp;

import com.example.application.models.NovedadesDTO;


public interface CatalogoService {

	NovedadesDTO novedades(Timestamp fecha);

}