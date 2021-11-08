package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.exception.EntityNotFoundException;
import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentPredicateBuilder;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import com.karnyshov.xmlbooks.repository.BookFragmentSortBuilder;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.BookFragmentFilterDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This service class encapsulated business logic related to {@link BookFragment} entity.
 */
@Service
public class BookFragmentService {
    private static final Pattern sortStringPattern = Pattern.compile("(asc|desc)\\((.*?)\\)");
    private final BookFragmentRepository repository;

    public BookFragmentService(BookFragmentRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieve book fragments according to specified parameters.
     * All parameters are optional, so if they are not present, all fragments will be retrieved.
     *
     * @param filterDto   {@link BookFragmentFilterDto} instance
     * @param pageContext {@link PageContext} object with pagination logic
     * @return {@link PaginationModel} object that contains list of {@link PaginationModel} objects
     */
    public PaginationModel<BookFragmentDto> find(BookFragmentFilterDto filterDto, PageContext pageContext) {
        Predicate predicate = new BookFragmentPredicateBuilder()
                .title(filterDto.getTitle())
                .build();

        PageRequest pageRequest;

        if (filterDto.getSortBy() != null) {
            BookFragmentSortBuilder sortBuilder = new BookFragmentSortBuilder();
            Map<String, Sort.Direction> sortMap = parseSortString(filterDto.getSortBy());
            sortMap.forEach((fieldName, direction) -> sortBuilder.byField(direction, fieldName));
            Sort sort = sortBuilder.build();

            pageRequest = pageContext.toPageRequest(sort);
        } else {
            pageRequest = pageContext.toPageRequest();
        }

        Page<BookFragmentDto> page = repository.findAll(predicate, pageRequest)
                .map(fragment -> new BookFragmentDto(fragment, false));
        return PaginationModel.fromPage(page);
    }

    /**
     * Retrieve book fragment by its unique id.
     *
     * @param id fragment id
     * @throws EntityNotFoundException in case when fragment with this id does not exist
     * @return {@link BookFragmentDto} object
     */
    public BookFragmentDto findById(String id) {
        BookFragment fragment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookFragment.class));
        return new BookFragmentDto(fragment, true);
    }

    /**
     * Save a book fragment.
     *
     * @param fragmentDto {@link BookFragmentDto} instance
     * @return {@link String} id of saved fragment
     */
    public String save(BookFragmentDto fragmentDto) {
        BookFragment fragment = fragmentDto.toFragment();
        BookFragment savedFragment = repository.save(fragment);
        return savedFragment.getId();
    }

    /**
     * Delete an existing fragment.
     *
     * @param id fragment id
     * @throws EntityNotFoundException in case when fragment with this id does not exist
     */
    public void delete(String id) {
        BookFragment fragment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookFragment.class));
        repository.delete(fragment);
        // set to NULL all foreign keys
        repository.findByNextFragmentId(id)
                .forEach(linkedFragment -> {
                    linkedFragment.setNextFragmentId(null);
                    repository.save(linkedFragment);
                });
    }

    private Map<String, Sort.Direction> parseSortString(String sortString) {
        Matcher matcher = sortStringPattern.matcher(sortString);
        Map<String, Sort.Direction> sortMap = new HashMap<>();

        while (matcher.find()) {
            String directionString = matcher.group(1);
            String fieldName = matcher.group(2);
            Sort.Direction direction = Sort.Direction.valueOf(directionString.toUpperCase());
            sortMap.put(fieldName, direction);
        }

        return sortMap;
    }
}
