package com.karnyshov.xmlbooks.controller;

import com.karnyshov.xmlbooks.exception.EntityNotFoundException;
import com.karnyshov.xmlbooks.exception.InvalidPageContextException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final String ENTITY_NOT_FOUND_ERROR_MESSAGE = "entity_not_found";
    private static final String INVALID_PAGE_NUMBER_MESSAGE = "invalid_page_number";
    private static final String INVALID_PAGE_SIZE_MESSAGE = "invalid_page_size";
    private static final String ERROR_MESSAGE = "errorMessage";

    private final ResourceBundleMessageSource errorMessageSource;

    public ControllerExceptionHandler(ResourceBundleMessageSource errorMessageSource) {
        this.errorMessageSource = errorMessageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e) {
        Class<?> entityClass = e.getEntityClass();
        String entityName = entityClass.getSimpleName();
        String errorMessage = String.format(getErrorMessage(ENTITY_NOT_FOUND_ERROR_MESSAGE), entityName);

        return buildErrorResponseEntity(NOT_FOUND, errorMessage);
    }

    @ExceptionHandler(InvalidPageContextException.class)
    public ResponseEntity<Object> handleInvalidPageContext(InvalidPageContextException e) {
        InvalidPageContextException.ErrorType errorType = e.getErrorType();
        int invalidValue = e.getInvalidValue();

        String errorMessage = switch (errorType) {
            case INVALID_PAGE_NUMBER -> getErrorMessage(INVALID_PAGE_NUMBER_MESSAGE);
            case INVALID_PAGE_SIZE -> getErrorMessage(INVALID_PAGE_SIZE_MESSAGE);
        };

        return buildErrorResponseEntity(BAD_REQUEST, String.format(errorMessage, invalidValue));
    }

    private String getErrorMessage(String errorMessageName) {
        Locale locale = LocaleContextHolder.getLocale();
        return errorMessageSource.getMessage(errorMessageName, null, locale);
    }

    private ResponseEntity<Object> buildErrorResponseEntity(HttpStatus status, String errorMessage) {
        Map<String, Object> body = new HashMap<>();
        body.put(ERROR_MESSAGE, errorMessage);

        return new ResponseEntity<>(body, status);
    }
}
