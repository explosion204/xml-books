package com.karnyshov.xmlbooks.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@QueryEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragment {
    @Id
    private String id;
    private String title;
    private LocalDateTime creationTime;
    private String body;
    private String nextFragmentId;
}
