package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.exception.EntityNotFoundException;
import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.DetailedBookFragmentDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BookFragmentService {
    private final BookFragmentRepository bookFragmentRepository;

    public BookFragmentService(BookFragmentRepository bookFragmentRepository) {
        this.bookFragmentRepository = bookFragmentRepository;
    }

    public PaginationModel<BookFragmentDto> getAll(PageContext pageContext) {
        PageRequest pageRequest = pageContext.toPageRequest();
        Page<BookFragmentDto> page = bookFragmentRepository.findAll(pageRequest)
                .map(BookFragmentDto::new);
        return PaginationModel.fromPage(page);
    }

    public DetailedBookFragmentDto get(Long id) {
        BookFragment fragment = bookFragmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookFragment.class));
        return new DetailedBookFragmentDto(fragment);
    }
}
