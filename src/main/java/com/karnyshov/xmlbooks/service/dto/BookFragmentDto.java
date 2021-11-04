package com.karnyshov.xmlbooks.service.dto;

import com.karnyshov.xmlbooks.model.BookFragment;
import lombok.Data;

@Data
public class BookFragmentDto {
    private Long id;
    private String type;
    private String title;

    public BookFragmentDto(BookFragment bookFragment) {
        id = bookFragment.getId();
        type = bookFragment.getType();
        title = bookFragment.getTitle();
    }
}
