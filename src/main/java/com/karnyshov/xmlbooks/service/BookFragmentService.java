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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BookFragmentService {
    private static final Logger logger = LoggerFactory.getLogger(BookFragmentService.class);
    private static final Pattern sortStringPattern = Pattern.compile("(asc|desc)\\((.*?)\\)");
    private final BookFragmentRepository repository;

    public BookFragmentService(BookFragmentRepository repository) {
        this.repository = repository;
    }

    public PaginationModel<BookFragmentDto> find(BookFragmentFilterDto filterDto, PageContext pageContext) {
        Predicate predicate = new BookFragmentPredicateBuilder()
                .type(filterDto.getType())
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

    public BookFragmentDto findById(String id) {
        BookFragment fragment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookFragment.class));
        return new BookFragmentDto(fragment, true);
    }

    public String save(BookFragmentDto fragmentDto) {
        BookFragment fragment = fragmentDto.toFragment();
        BookFragment savedFragment = repository.save(fragment);
        return savedFragment.getId();
    }

    public void delete(String id) {
        BookFragment fragment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookFragment.class));
        repository.delete(fragment);
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

            try {
                Sort.Direction direction = Sort.Direction.valueOf(directionString.toUpperCase());
                sortMap.put(fieldName, direction);
            } catch (EnumConstantNotPresentException e) {
                logger.error("Invalid sorting direction '{}'", directionString);
            }
        }

        return sortMap;
    }
}
