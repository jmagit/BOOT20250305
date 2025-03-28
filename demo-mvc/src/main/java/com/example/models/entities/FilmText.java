package com.example.models.entities;

import java.io.Serializable;
import jakarta.persistence.*;


/**
 * The persistent class for the FILM_TEXT database table.
 * 
 */
@Entity
@Table(name="FILM_TEXT")
@NamedQuery(name="FilmText.findAll", query="SELECT f FROM FilmText f")
public class FilmText implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FILM_ID")
	private long filmId;

	@Lob
	private String description;

	private String title;

	public FilmText() {
	}

	public long getFilmId() {
		return this.filmId;
	}

	public void setFilmId(long filmId) {
		this.filmId = filmId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}