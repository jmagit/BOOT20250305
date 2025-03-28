package com.example.domains.event;

public record DomainEvent(String entity, int pk, String property, Object old, Object current) {

}
