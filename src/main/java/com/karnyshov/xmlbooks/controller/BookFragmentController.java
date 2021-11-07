package com.karnyshov.xmlbooks.controller;

import com.karnyshov.xmlbooks.exception.EntityNotFoundException;
import com.karnyshov.xmlbooks.exception.InvalidPageContextException;
import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.service.BookFragmentService;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.BookFragmentFilterDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

/**
 * This class contains public REST API endpoints related to {@link BookFragment} entity.
 */
@RestController
@RequestMapping("/books")
public class BookFragmentController {
    private final BookFragmentService bookFragmentService;

    public BookFragmentController(BookFragmentService bookFragmentService) {
        this.bookFragmentService = bookFragmentService;
    }

    /**
     * Retrieve book fragments according to specified parameters.
     * All parameters are optional, so if they are not present, all fragments will be retrieved.
     *
     * @param filterDto {@link BookFragmentFilterDto} instance
     * @param page      the page
     * @param pageSize  the page size
     * @throws InvalidPageContextException if passed page or page size values are invalid
     * @return JSON {@link ResponseEntity} object that contains list of {@link PaginationModel} objects
     */
    @GetMapping
    public ResponseEntity<PaginationModel<BookFragmentDto>> getFragments(
            @ModelAttribute BookFragmentFilterDto filterDto,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    ) {
        PageContext pageContext = PageContext.of(page, pageSize);
        PaginationModel<BookFragmentDto> paginationModel = bookFragmentService.find(filterDto, pageContext);
        return new ResponseEntity<>(paginationModel, OK);
    }

    /**
     * Retrieve book fragment by its unique id.
     *
     * @param id fragment id
     * @throws EntityNotFoundException in case when certificate with this id does not exist
     * @return JSON {@link ResponseEntity} object that contains {@link BookFragmentDto} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookFragmentDto> getFragment(@PathVariable("id") String id) {
        BookFragmentDto fragmentDto = bookFragmentService.findById(id);
        return new ResponseEntity<>(fragmentDto, OK);
    }

    /**
     * Delete an existing fragment.
     *
     * @param id fragment id
     * @throws EntityNotFoundException in case when certificate with this id does not exist
     * @return empty {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFragment(@PathVariable("id") String id) {
        bookFragmentService.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
