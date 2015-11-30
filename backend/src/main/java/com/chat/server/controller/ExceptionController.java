package com.chat.server.controller;

import com.chat.server.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on 30.11.2015.
 */

@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="No item with requested id")
    @ExceptionHandler(ObjectNotFoundException.class)
    public void handleBadRequestException() {

    }

}
