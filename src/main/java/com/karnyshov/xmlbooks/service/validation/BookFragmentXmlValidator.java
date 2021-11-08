package com.karnyshov.xmlbooks.service.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.IOException;
import java.net.URL;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

@Component
public class BookFragmentXmlValidator {
    @Value("classpath:assets/schema.xsd")
    private Resource schemaResource;
    private URL schemaUrl;

    @PostConstruct
    void init() throws IOException {
        schemaUrl = schemaResource.getURL();
    }

    public boolean validateXmlFile(String xmlPath) {
        SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = factory.newSchema(schemaUrl);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(xmlPath);
            validator.validate(source);
        } catch (SAXException | IOException e) {
            return false;
        }

        return true;
    }
}
