package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.service.validation.BookFragmentXmlValidator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(value = SpringExtension.class)
class BookFragmentXmlValidatorTest {
    @Autowired
    private BookFragmentXmlValidator validator;

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testValidateXmlFile(boolean expected, String xmlPath) {
        boolean actual = validator.validateXmlFile(xmlPath);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideTestData() throws FileNotFoundException {
        String validXmlPath = ResourceUtils.getFile("classpath:validation/valid.xml")
                .getAbsolutePath();
        String invalidXmlPath = ResourceUtils.getFile("classpath:validation/invalid.xml")
                .getAbsolutePath();

        return Stream.of(
                Arguments.of(true, validXmlPath),
                Arguments.of(false, invalidXmlPath)
        );
    }
}
