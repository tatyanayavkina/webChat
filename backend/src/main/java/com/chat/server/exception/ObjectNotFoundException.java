package com.chat.server.exception;

public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException(Class clazz, Object id) {
		super(String.format("Сущность %s с идентификаторм %s не найдена.", new Object[]{clazz, id}));
	}

}
