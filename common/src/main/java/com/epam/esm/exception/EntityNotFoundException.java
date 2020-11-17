package com.epam.esm.exception;

public class EntityNotFoundException extends RuntimeException {
    private String message;
    private long resourceId;
    private long errorCode;

    public EntityNotFoundException(){}

    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }

    public EntityNotFoundException(Throwable throwable){
        super(throwable);
    }

    public EntityNotFoundException(String message, long resourceId, long errorCode){
        this.message = message;
        this.resourceId = resourceId;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public long getResourceId() {
        return resourceId;
    }

    public long getErrorCode() {
        return errorCode;
    }
}
