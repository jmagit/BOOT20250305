package com.example.domains.entities.dtos;

import org.springframework.data.rest.core.config.Projection;

import com.example.domains.entities.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@Schema(name = "Actor", description = "Datos del actor")
@Projection(name = "actorDTO", types = { Actor.class })
public class ActorDTO {
//	@JsonProperty("id")
	private int actorId;
	@Schema(description = "Nombre del actor", example = "Pepito", required = true, minLength = 2, maxLength = 45)
	private String firstName;
	@NotBlank
	@Size(min = 2, max = 45)
	@Schema(description = "Apellidos del actor", example = "Grillo")
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
