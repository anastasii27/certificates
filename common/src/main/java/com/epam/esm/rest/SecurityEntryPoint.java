package com.epam.esm.rest;

import com.epam.esm.model.Error;
import com.epam.esm.utils.ExceptionHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.web.servlet.support.RequestContextUtils.getLocale;

@Component
public class SecurityEntryPoint implements AuthenticationEntryPoint {
    private static final int ERROR_CODE = 40100;
    private static final String EXCEPTION_KEY = "exception.auth.not_authorized_1";
    @Autowired
    private ExceptionHandlerUtils exceptionHandlerUtils;
    @Autowired
    private MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        String errorMessage;

        if(e instanceof BadCredentialsException || e instanceof InsufficientAuthenticationException){
            errorMessage = messageSource.getMessage(EXCEPTION_KEY, null, getLocale(request));
        }else {
            errorMessage = exceptionHandlerUtils.extractLocalizedMessage(e, getLocale(request));
        }

        Error error = new Error(ERROR_CODE, errorMessage);
        exceptionHandlerUtils.sendJsonResponse(error, response, SC_UNAUTHORIZED);
    }
}