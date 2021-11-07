package com.karnyshov.xmlbooks.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@QueryEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragment {
    @Id
    private String id;
    private String type;
    private String title;
    private String body;
    private String nextFragmentId;
}
