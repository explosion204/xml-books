package com.karnyshov.xmlbooks.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.karnyshov.xmlbooks.model.BookFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragmentDto {
    private String id;
    private String type;
    private String title;

    @JsonInclude(NON_NULL)
    private String body;
    private String nextFragmentId;

    public BookFragmentDto(BookFragment bookFragment, boolean includeBody) {
        id = bookFragment.getId();
        type = bookFragment.getType();
        title = bookFragment.getTitle();
        nextFragmentId = bookFragment.getNextFragmentId();

        if (includeBody) {
            body = bookFragment.getBody();
        }
    }

    public BookFragment toFragment() {
        BookFragment fragment = new BookFragment();

        fragment.setId(id);
        fragment.setType(type);
        fragment.setTitle(title);
        fragment.setBody(body);
        fragment.setNextFragmentId(nextFragmentId);

        return fragment;
    }
}
