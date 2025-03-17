package com.example.domains.entities.dtos;

import com.example.domains.entities.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
public class ActorDTO {
//	@JsonProperty("id")
	private int actorId;
	private String firstName;
	private String lastName;
	
	public static ActorDTO from(Actor source) {
		return new ActorDTO(source.getActorId(), source.getFirstName(), source.getLastName());
	}
	
	public static Actor from(ActorDTO source) {
		return new Actor(source.getActorId(), source.getFirstName(), source.getLastName());
	}

//	public ActorDTO(int actorId, String firstName, String lastName) {
//		super();
//		this.id = actorId;
//		this.nombre = firstName;
//		this.apellidos = lastName;
//	}
}
