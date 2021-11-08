package com.karnyshov.xmlbooks.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.karnyshov.xmlbooks.model.BookFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragmentDto {
    private String id;
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime creationTime;

    @JsonInclude(NON_NULL)
    private String body;
    private String nextFragmentId;

    public BookFragmentDto(BookFragment bookFragment, boolean includeBody) {
        id = bookFragment.getId();
        title = bookFragment.getTitle();
        creationTime = bookFragment.getCreationTime();
        nextFragmentId = bookFragment.getNextFragmentId();

        if (includeBody) {
            body = bookFragment.getBody();
        }
    }

    public BookFragment toFragment() {
        BookFragment fragment = new BookFragment();

        fragment.setId(id);
        fragment.setTitle(title);
        fragment.setCreationTime(creationTime);
        fragment.setBody(body);
        fragment.setNextFragmentId(nextFragmentId);

        return fragment;
    }
}
