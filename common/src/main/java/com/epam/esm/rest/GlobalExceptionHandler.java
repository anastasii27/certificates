package com.epam.esm.rest;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.InvalidDataInputException;
import com.epam.esm.model.Error;
import com.epam.esm.utils.ExceptionHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String SEPARATOR = ": ";
    private static final int BAD_REQ_ERROR_CODE = 40000;
    @Autowired
    private ExceptionHandlerUtils exceptionHandlerUtils;
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFound(EntityNotFoundException e, Locale locale) {
        String errorMessage = exceptionHandlerUtils.extractLocalizedMessage(e, locale);

        return new Error(e.getErrorCode(), errorMessage + e.getResourceId());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleConflict(EntityAlreadyExistsException e, Locale locale) {
        String errorMessage = exceptionHandlerUtils.extractLocalizedMessage(e, locale);

        return new Error(e.getErrorCode(), errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleIllegalArgument(IllegalArgumentException e, Locale locale) {
        String errorMessage = exceptionHandlerUtils.extractLocalizedMessage(e, locale);

        return new Error(BAD_REQ_ERROR_CODE,errorMessage);
    }

    @ExceptionHandler(InvalidDataInputException.class)
    public  ResponseEntity<Object>  handleInvalidDataInput(InvalidDataInputException e, Locale locale) {
        List<String> errors = new ArrayList<>();

        for (final FieldError error : e.getBindingResult().getFieldErrors()) {
            String errorMessage = messageSource.getMessage(error.getCode(), null,  locale);
            errors.add(error.getField() + SEPARATOR + errorMessage);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();

        for (final FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + SEPARATOR + error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, status);
    }
}
