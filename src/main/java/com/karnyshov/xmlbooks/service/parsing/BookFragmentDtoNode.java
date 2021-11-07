package com.karnyshov.xmlbooks.service.parsing;

import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import lombok.Data;


/**
 * This node class is a part of parsed fragments graph.
 */
@Data
public class BookFragmentDtoNode {
    private BookFragmentDto bookFragmentDto;
    private String id;
    private boolean isSaved = false;

    private BookFragmentDtoNode nextNode;
}
