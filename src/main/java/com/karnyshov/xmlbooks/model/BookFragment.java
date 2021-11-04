package com.karnyshov.xmlbooks.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class BookFragment {
    @Id
    private Long id;
    private String type;
    private String title;
    private String body;
}
