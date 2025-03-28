package com.example.models.entities;

import java.io.Serializable;
import jakarta.persistence.*;

/**
 * The primary key class for the FILM_ACTOR database table.
 * 
 */
@Embeddable
public class FilmActorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ACTOR_ID", insertable=false, updatable=false)
	private long actorId;

	@Column(name="FILM_ID", insertable=false, updatable=false)
	private long filmId;

	public FilmActorPK() {
	}
	public long getActorId() {
		return this.actorId;
	}
	public void setActorId(long actorId) {
		this.actorId = actorId;
	}
	public long getFilmId() {
		return this.filmId;
	}
	public void setFilmId(long filmId) {
		this.filmId = filmId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FilmActorPK)) {
			return false;
		}
		FilmActorPK castOther = (FilmActorPK)other;
		return 
			(this.actorId == castOther.actorId)
			&& (this.filmId == castOther.filmId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) (this.actorId ^ (this.actorId >>> 32)));
		hash = hash * prime + ((int) (this.filmId ^ (this.filmId >>> 32)));
		
		return hash;
	}
}