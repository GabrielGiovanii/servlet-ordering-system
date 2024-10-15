package com.servlet_ordering_system.models.services.contracts;

public interface DtoConverter<OBJ> {

    OBJ dtoToObject(Object dto);
}
