package com.karnyshov.xmlbooks.controller;

import com.karnyshov.xmlbooks.exception.InvalidPageContextException;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ControllerExceptionHandlerTest {
    @Autowired
    private ControllerExceptionHandler exceptionHandler;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    private Stream<Arguments> provideTestData() {
        Locale locale = LocaleContextHolder.getLocale();
        String invalidPageNumberMessage = messageSource.getMessage("invalid_page_number", null, locale);
        String invalidPageSizeMessage = messageSource.getMessage("invalid_page_size", null, locale);

        return Stream.of(
                Arguments.of(InvalidPageContextException.ErrorType.INVALID_PAGE_NUMBER, invalidPageNumberMessage, -1),
                Arguments.of(InvalidPageContextException.ErrorType.INVALID_PAGE_SIZE, invalidPageSizeMessage, 51)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    void handleInvalidPageContext(InvalidPageContextException.ErrorType errorType, String errorMessage, int invalidValue) {
        InvalidPageContextException exception = new InvalidPageContextException(errorType, invalidValue);

        Map<String, Object> body = new HashMap<>();
        body.put("errorMessage", String.format(errorMessage, invalidValue));
        ResponseEntity<Object> expectedResponseEntity = new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);

        ResponseEntity<Object> actualResponseEntity = exceptionHandler.handleInvalidPageContext(exception);

        assertEquals(expectedResponseEntity, actualResponseEntity);
    }
}