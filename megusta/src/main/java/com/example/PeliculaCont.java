package com.example;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("peliculas")
public class PeliculaCont {
	@Id
	int id;
	int cont;
	
	public PeliculaCont() {}
	public PeliculaCont(int id, int cont) {
		super();
		this.id = id;
		this.cont = cont;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCont() {
		return cont;
	}
	public void setCont(int cont) {
		this.cont = cont;
	}
	public void addCont() {
		this.cont++;
	}
	public void addCont(int cont) {
		this.cont += cont;
	}
	
	public int hashCode() {
		return Objects.hash(cont, id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeliculaCont other = (PeliculaCont) obj;
		return cont == other.cont && id == other.id;
	}
	@Override
	public String toString() {
		return "PeliculaCont [id=" + id + ", cont=" + cont + "]";
	}
	
	
}
