package com.chat.server.controller;

import com.chat.server.exception.AlreadyExistsException;
import com.chat.server.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No item with requested id")
    @ExceptionHandler(ObjectNotFoundException.class)
    public void handleObjectNotFoundException() {

    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Item already exists")
    @ExceptionHandler(AlreadyExistsException.class)
    public void handleAlreadyExistsException() {

    }


}
