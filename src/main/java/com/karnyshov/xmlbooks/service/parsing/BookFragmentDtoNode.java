package com.karnyshov.xmlbooks.service.parsing;

import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import lombok.Data;

@Data
public class BookFragmentDtoNode {
    private BookFragmentDto bookFragmentDto;
    private String id;
    private boolean isSaved = false;

    private BookFragmentDtoNode nextNode;
}
