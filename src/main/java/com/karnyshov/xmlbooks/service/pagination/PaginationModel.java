package com.karnyshov.xmlbooks.service.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginationModel<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalEntities;

    public static <T> PaginationModel<T> fromPage(Page<T> page) {
        PaginationModel<T> paginationModel = new PaginationModel<>();

        paginationModel.data = page.getContent();
        paginationModel.pageNumber = page.getNumber() + 1; // zero based
        paginationModel.pageSize = page.getSize();
        paginationModel.totalPages = page.getTotalPages();
        paginationModel.totalEntities = page.getTotalElements();

        return paginationModel;
    }
}
