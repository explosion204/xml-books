package com.karnyshov.xmlbooks.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFragmentFilterDto {
    private String type;
    private String title;
    private String sortBy;
}
