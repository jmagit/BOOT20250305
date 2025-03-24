package com.example.domains.entities.dtos;


import org.springframework.data.rest.core.config.Projection;
import com.example.domains.entities.Category;
import com.example.domains.entities.Language;

@Projection(name = "name-only", types = { Language.class, Category.class })
public interface NameOnly {
	String getName();
}
