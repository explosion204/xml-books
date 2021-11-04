package com.karnyshov.xmlbooks.service.dto;

import com.karnyshov.xmlbooks.model.BookFragment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragmentDto {
    protected Long id;
    protected String type;
    protected String title;

    public BookFragmentDto(BookFragment bookFragment) {
        id = bookFragment.getId();
        type = bookFragment.getType();
        title = bookFragment.getTitle();
    }
}
