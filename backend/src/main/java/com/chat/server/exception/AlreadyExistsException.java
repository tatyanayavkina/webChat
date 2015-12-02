package com.chat.server.exception;

public class AlreadyExistsException extends Exception {

	public AlreadyExistsException(Class clazz) {
		super(String.format("Сущность %s с идентификаторм уже существует.", new Object[]{clazz}));
	}

}
