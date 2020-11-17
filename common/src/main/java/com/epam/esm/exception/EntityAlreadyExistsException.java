package com.epam.esm.exception;

public class EntityAlreadyExistsException extends RuntimeException{
    private String message;
    private long errorCode;

    public EntityAlreadyExistsException(){}

    public EntityAlreadyExistsException(String message){
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable throwable){
        super(message, throwable);
    }

    public EntityAlreadyExistsException (Throwable throwable){
        super(throwable);
    }

    public EntityAlreadyExistsException (String message, long errorCode){
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public long getErrorCode() {
        return errorCode;
    }
}
