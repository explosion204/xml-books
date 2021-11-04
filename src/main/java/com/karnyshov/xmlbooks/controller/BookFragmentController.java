package com.karnyshov.xmlbooks.controller;

import com.karnyshov.xmlbooks.service.BookFragmentService;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.DetailedBookFragmentDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/books")
public class BookFragmentController {
    private final BookFragmentService bookFragmentService;

    public BookFragmentController(BookFragmentService bookFragmentService) {
        this.bookFragmentService = bookFragmentService;
    }

    @GetMapping
    public ResponseEntity<PaginationModel<BookFragmentDto>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        PageContext pageContext = PageContext.of(page, pageSize);
        PaginationModel<BookFragmentDto> paginationModel = bookFragmentService.findAll(pageContext);
        return new ResponseEntity<>(paginationModel, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedBookFragmentDto> get(@PathVariable("id") Long id) {
        DetailedBookFragmentDto dto = bookFragmentService.findById(id);
        return new ResponseEntity<>(dto, OK);
    }
}
