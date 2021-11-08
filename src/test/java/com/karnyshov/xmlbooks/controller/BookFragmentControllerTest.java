package com.karnyshov.xmlbooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import com.karnyshov.xmlbooks.service.pagination.PageContext;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookFragmentControllerTest {
    private static final LocalDateTime DUMMY_TIME = LocalDateTime.now(UTC);
    private static List<BookFragment> fragmentList;
    private static ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeAll
    static void setup() {
        BookFragment firstFragment = new BookFragment();
        BookFragment secondFragment = new BookFragment();
        BookFragment thirdFragment = new BookFragment();

        firstFragment.setTitle("afragment1 title");
        firstFragment.setBody("fragment1 body");
        firstFragment.setCreationTime(DUMMY_TIME);

        secondFragment.setTitle("cfragment2 title");
        secondFragment.setBody("fragment2 body");
        secondFragment.setCreationTime(DUMMY_TIME);

        thirdFragment.setTitle("bfragment3 title");
        thirdFragment.setBody("fragment3 body");
        thirdFragment.setCreationTime(DUMMY_TIME);

        fragmentList = List.of(firstFragment, secondFragment, thirdFragment);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @ParameterizedTest
    @MethodSource("provideGetFragmentsTestData")
    void testGetFragments(List<BookFragmentDto> expectedDtoList, Integer pageNumber, Integer pageSize,
                Map<String, String> queryParams) throws Exception {
        PageContext pageContext = PageContext.of(pageNumber, pageSize);
        int total = expectedDtoList.size();
        Page<BookFragmentDto> page = new PageImpl<>(expectedDtoList, pageContext.toPageRequest(), total);

        PaginationModel<BookFragmentDto> expectedModel = PaginationModel.fromPage(page);
        String expectedResponse = objectMapper.writeValueAsString(expectedModel);

        MockHttpServletRequestBuilder requestBuilder = get("/books");
        for (Map.Entry<String, String> param : queryParams.entrySet()) {
            String paramName = param.getKey();
            String paramValue = param.getValue();
            requestBuilder = requestBuilder.queryParam(paramName, paramValue);
        }

        mockMvc.perform(requestBuilder)
                .andExpect(content().json(expectedResponse));
    }


    @Test
    void testGetFragmentById() throws Exception {
        BookFragmentDto expectedDto = provideFragmentDtoList().get(0);
        String expectedResponse = objectMapper.writeValueAsString(expectedDto);

        mockMvc.perform(get("/books/" + expectedDto.getId()))
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void testGetFragmentByIdWhenNotFound() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().is(404));
    }

    @Test
    void testDeleteFragment() throws Exception {
        BookFragment fragment = fragmentList.get(0);
        BookFragment savedFragment = mongoTemplate.save(fragment);

        mockMvc.perform(delete("/books/" + savedFragment.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void testDeleteFragmentWhenNotFound() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().is(404));

        int expectedSize = 0;
        int actualSize = mongoTemplate.findAll(BookFragmentDto.class).size();
        assertEquals(expectedSize, actualSize);
    }

    private Stream<Arguments> provideGetFragmentsTestData() {
        List<BookFragmentDto> fragmentDtoList = provideFragmentDtoList();

        Arguments firstArg = Arguments.of(fragmentDtoList, null, null, Collections.emptyMap());
        Arguments secondArg = Arguments.of(fragmentDtoList, 1, 10, Collections.emptyMap());

        List<BookFragmentDto> thirdExpectedList = fragmentDtoList.stream()
                .filter(fragmentDto -> fragmentDto.getTitle().contains("afragment1 title"))
                .toList();
        Arguments thirdArg = Arguments.of(thirdExpectedList, null, null, Map.of("title", "afragment1 title"));

        List<BookFragmentDto> fourthExpectedList = fragmentDtoList.stream()
                .filter(fragmentDto -> fragmentDto.getTitle().contains("hello there"))
                .toList();
        Arguments fourthArg = Arguments.of(fourthExpectedList, null, null, Map.of("title", "hello there"));

        List<BookFragmentDto> fifthExpectedList = fragmentDtoList.stream()
                .sorted(Comparator.comparing(BookFragmentDto::getTitle))
                .toList();
        Arguments fifthArg = Arguments.of(fifthExpectedList, null, null, Map.of("sortBy", "asc(title)"));

        return Stream.of(
                firstArg, secondArg, thirdArg, fourthArg, fifthArg
        );
    }

    private List<BookFragmentDto> provideFragmentDtoList() {
        List<BookFragmentDto> fragmentDtoList = new ArrayList<>();
        BookFragment firstFragment = fragmentList.get(0);
        BookFragment secondFragment = fragmentList.get(1);
        BookFragment thirdFragment = fragmentList.get(2);

        mongoTemplate.dropCollection("bookFragment");
        BookFragment savedThirdFragment = mongoTemplate.save(thirdFragment);

        secondFragment.setNextFragmentId(savedThirdFragment.getId());
        BookFragment savedSecondFragment = mongoTemplate.save(secondFragment);

        firstFragment.setNextFragmentId(savedSecondFragment.getId());
        BookFragment savedFirstFragment = mongoTemplate.save(firstFragment);

        fragmentDtoList.add(new BookFragmentDto(savedFirstFragment, false));
        fragmentDtoList.add(new BookFragmentDto(savedSecondFragment, false));
        fragmentDtoList.add(new BookFragmentDto(savedThirdFragment, false));

        return fragmentDtoList;
    }
}
