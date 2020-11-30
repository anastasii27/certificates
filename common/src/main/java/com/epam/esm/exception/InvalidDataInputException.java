package com.epam.esm.exception;

import org.springframework.validation.BindingResult;

public class InvalidDataInputException extends RuntimeException {
    private BindingResult bindingResult;

    public InvalidDataInputException(){}

    public InvalidDataInputException(String message){
        super(message);
    }

    public InvalidDataInputException(String message, Throwable throwable){
        super(message, throwable);
    }

    public InvalidDataInputException(Throwable throwable){
        super(throwable);
    }

    public InvalidDataInputException(BindingResult bindingResult){
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
