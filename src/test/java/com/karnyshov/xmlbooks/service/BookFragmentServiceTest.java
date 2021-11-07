package com.karnyshov.xmlbooks.service;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.dto.BookFragmentFilterDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class BookFragmentServiceTest {
    private static final String DUMMY_STRING = "dummy";

    @InjectMocks
    private BookFragmentService service;

    @Mock
    private BookFragmentRepository repository;

    @Captor
    private ArgumentCaptor<Sort> sortCaptor;

    @BeforeAll
    static void setUp() {
        MockitoAnnotations.openMocks(BookFragmentServiceTest.class);
    }

    @Test
    void testFindWithoutSort() {
        BookFragmentFilterDto filterDto = new BookFragmentFilterDto();

        List<BookFragment> bookFragments = provideBookFragmentList();
        PageContext pageContext = PageContext.of(1, bookFragments.size());
        Page<BookFragment> page = new PageImpl<>(bookFragments);
        when(repository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(page);

        List<BookFragmentDto> expectedList = provideBookFragmentDtoList();
        List<BookFragmentDto> actualList = service.find(filterDto, pageContext)
                .getData();

        assertEquals(expectedList, actualList);
    }

    @Test
    void testFind() {
        String sortBy = "asc(title),desc(section)";
        BookFragmentFilterDto filterDto = new BookFragmentFilterDto(null, null, sortBy);
        List<BookFragment> bookFragments = provideBookFragmentList();
        PageContext pageContext = spy(PageContext.of(1, bookFragments.size()));
        Page<BookFragment> page = new PageImpl<>(bookFragments);
        when(repository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(page);

        service.find(filterDto, pageContext);

        verify(pageContext).toPageRequest(sortCaptor.capture());
        Sort capturedSort = sortCaptor.getValue();

        Map<String, Sort.Direction> expectedMap = Map.of("title", ASC, "section", DESC);
        Map<String, Sort.Direction> actualMap = capturedSort.get()
                .collect(Collectors.toMap(Sort.Order::getProperty, Sort.Order::getDirection));
        assertEquals(expectedMap, actualMap);
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

    @Test
    void testDelete() {
        BookFragment fragment = provideBookFragmentList().get(0);
        when(repository.findById(fragment.getId())).thenReturn(Optional.of(fragment));

        List<BookFragment> linkedFragments = provideBookFragmentList()
                .stream()
                .skip(1)
                .toList();
        when(repository.findByNextFragmentId(fragment.getId())).thenReturn(linkedFragments);

        service.delete(fragment.getId());

        verify(repository).delete(fragment);
        verify(repository).findByNextFragmentId(fragment.getId());

        assertTrue(linkedFragments.stream().anyMatch(linkedFragment -> linkedFragment.getNextFragmentId() == null));
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
