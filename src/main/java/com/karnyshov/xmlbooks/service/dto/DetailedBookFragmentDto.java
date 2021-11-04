package com.karnyshov.xmlbooks.service.dto;

import com.karnyshov.xmlbooks.model.BookFragment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DetailedBookFragmentDto extends BookFragmentDto {
    private final String body;

    public DetailedBookFragmentDto(BookFragment bookFragment) {
        super(bookFragment);
        body = bookFragment.getBody();
    }
}
