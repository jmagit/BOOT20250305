package com.example.models.entities;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the FILM_CATEGORY database table.
 * 
 */
@Embeddable
public class FilmCategoryPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="FILM_ID", insertable=false, updatable=false)
	private long filmId;

	@Column(name="CATEGORY_ID", insertable=false, updatable=false)
	private long categoryId;

	public FilmCategoryPK() {
	}
	public long getFilmId() {
		return this.filmId;
	}
	public void setFilmId(long filmId) {
		this.filmId = filmId;
	}
	public long getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FilmCategoryPK)) {
			return false;
		}
		FilmCategoryPK castOther = (FilmCategoryPK)other;
		return 
			(this.filmId == castOther.filmId)
			&& (this.categoryId == castOther.categoryId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.filmId ^ (this.filmId >>> 32)));
		hash = hash * prime + ((int) (this.categoryId ^ (this.categoryId >>> 32)));
		
		return hash;
	}
}