package com.example.application.models;

import java.util.List;

import com.example.domains.entities.Category;
import com.example.domains.entities.Film;
import com.example.domains.entities.Language;
import com.example.domains.entities.models.ActorDTO;
import com.example.domains.entities.models.FilmShortDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class NovedadesDTO {
	private List<FilmShortDTO> films;
	private List<ActorDTO> actors;
	private List<Category> categories;
	private List<Language> languages;
	
}
