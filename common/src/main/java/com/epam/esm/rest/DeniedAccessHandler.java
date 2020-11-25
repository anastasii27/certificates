package com.epam.esm.rest;

import com.epam.esm.model.Error;
import com.epam.esm.utils.ExceptionHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

@Component
public class DeniedAccessHandler implements AccessDeniedHandler {
    private static final int ERROR_CODE = 40300;
    private static final String EXCEPTION_KEY = "exception.auth.forbidden";
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ExceptionHandlerUtils exceptionHandlerUtils;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        Locale locale = RequestContextUtils.getLocale(request);
        String errorMessage = messageSource.getMessage(EXCEPTION_KEY, null, locale);
        Error error = new Error(ERROR_CODE, errorMessage);

        exceptionHandlerUtils.sendJsonResponse(error, response, SC_FORBIDDEN);
    }
}
