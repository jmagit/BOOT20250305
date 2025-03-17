package com.example.domains.event;

import java.lang.reflect.Field;

import org.springframework.data.mapping.MappingException;

import jakarta.persistence.Id;

public record EntityChangedEvent(Type type, String entity, Object key) {
	public static enum Type {
		ADD, MODIFY, REMOVE, OTHERS
	}
	public static EntityChangedEvent asAdd(Object entity) {
		return new EntityChangedEvent(Type.ADD, entity.getClass().getSimpleName(), getId(entity));
	}
	public static EntityChangedEvent asModify(Object entity) {
		return new EntityChangedEvent(Type.MODIFY, entity.getClass().getSimpleName(), getId(entity));
	}
	public static EntityChangedEvent asRemove(Object entity) {
		return new EntityChangedEvent(Type.REMOVE, entity.getClass().getSimpleName(), getId(entity));
	}
	public static EntityChangedEvent asRemove(Object entity, Object key) {
		return new EntityChangedEvent(Type.REMOVE, entity.getClass().getSimpleName(), key);
	}
	public static EntityChangedEvent asRemove(String entity, Object key) {
		return new EntityChangedEvent(Type.REMOVE, entity, key);
	}
	public static EntityChangedEvent asOthers(Object entity) {
		return new EntityChangedEvent(Type.OTHERS, entity.getClass().getSimpleName(), getId(entity));
	}

	private static Object getId(Object entity) {
		try {
		    for (Field field: entity.getClass().getDeclaredFields()) {
		        if (field.getAnnotation(Id.class) != null) {
		        	field.setAccessible(true);
		        	var value = field.get(entity);
		        	field.setAccessible(false);
		            return value;
		        }
		    }
		} catch (Exception e) {
		    throw new MappingException("Entity does not have an id field.", e);
		}
	    throw new MappingException("Entity does not have an id field.");
	}

}
