package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.DetailedBookFragmentDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookFragmentServiceTest {
    private static final String DUMMY_STRING = "dummy";

    @InjectMocks
    private BookFragmentService service;

    @Mock
    private BookFragmentRepository repository;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(BookFragmentServiceTest.class);
    }

    @Test
    void testFindAll() {
        List<BookFragment> bookFragments = provideBookFragmentList();
        PageContext pageContext = PageContext.of(1, bookFragments.size());
        PageRequest pageRequest = pageContext.toPageRequest();
        Page<BookFragment> page = new PageImpl<>(bookFragments);
        when(repository.findAll(pageRequest)).thenReturn(page);

        List<BookFragmentDto> expectedList = provideBookFragmentDtoList();
        List<BookFragmentDto> actualList = service.findAll(pageContext)
                .getData();

        assertEquals(expectedList, actualList);
    }

    @Test
    void testFindById() {
        BookFragment bookFragment = provideBookFragmentList().get(0);
        when(repository.findById(bookFragment.getId())).thenReturn(Optional.of(bookFragment));

        DetailedBookFragmentDto expectedDto = provideDetailedBookFragmentDto();
        DetailedBookFragmentDto actualDto = service.findById(bookFragment.getId());

        assertEquals(expectedDto, actualDto);
    }

    private List<BookFragment> provideBookFragmentList() {
        return new ArrayList<>() {{
            add(new BookFragment(1L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragment(2L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragment(3L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragment(4L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragment(5L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING));
        }};
    }

    private List<BookFragmentDto> provideBookFragmentDtoList() {
        return new ArrayList<>() {{
            add(new BookFragmentDto(1L, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragmentDto(2L, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragmentDto(3L, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragmentDto(4L, DUMMY_STRING, DUMMY_STRING));
            add(new BookFragmentDto(5L, DUMMY_STRING, DUMMY_STRING));
        }};
    }

    private DetailedBookFragmentDto provideDetailedBookFragmentDto() {
        BookFragment bookFragment = new BookFragment(1L, DUMMY_STRING, DUMMY_STRING, DUMMY_STRING);
        return new DetailedBookFragmentDto(bookFragment);
    }
}
