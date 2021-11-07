package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
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

        BookFragmentDto expectedDto = provideBookFragmentDtoList().get(0);
        BookFragmentDto actualDto = service.findById(bookFragment.getId());

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void testSave() {
        BookFragmentDto fragmentDto = provideBookFragmentDtoList().get(0);
        BookFragment fragment = provideBookFragmentList().get(0);
        when(repository.save(fragment)).thenReturn(fragment);

        String expectedId = fragmentDto.getId();
        String actualId = service.save(fragmentDto);

        assertEquals(expectedId, actualId);
    }

    private List<BookFragment> provideBookFragmentList() {
        return new ArrayList<>() {{
            add(new BookFragment("1", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragment("2", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragment("3", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragment("4", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragment("5", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
        }};
    }

    private List<BookFragmentDto> provideBookFragmentDtoList() {
        return new ArrayList<>() {{
            add(new BookFragmentDto("1", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragmentDto("2", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragmentDto("3", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragmentDto("4", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
            add(new BookFragmentDto("5", DUMMY_STRING, DUMMY_STRING, null, DUMMY_STRING));
        }};
    }
}
