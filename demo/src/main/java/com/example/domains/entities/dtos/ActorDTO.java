package com.example.domains.entities.dtos;

import com.example.domains.entities.Actor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
public class ActorDTO {
	private int actorId;
	private String firstName;
	private String lastName;
	
	public static ActorDTO from(Actor source) {
		return new ActorDTO(source.getActorId(), source.getFirstName(), source.getLastName());
	}
	
	public static Actor from(ActorDTO source) {
		return new Actor(source.getActorId(), source.getFirstName(), source.getLastName());
	}
}
