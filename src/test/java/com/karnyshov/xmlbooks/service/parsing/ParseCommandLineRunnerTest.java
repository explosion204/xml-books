package com.karnyshov.xmlbooks.service.parsing;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.repository.BookFragmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(value = { SpringExtension.class, MockitoExtension.class })
class ParseCommandLineRunnerTest {
    private static final String DUMMY_STRING = "dummy";
    private static final LocalDateTime DUMMY_TIME = LocalDateTime.now(UTC);

    @Autowired
    private ParseCommandLineRunner commandLineRunner;

    @Autowired
    private BookFragmentRepository repository;

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testRun(List<BookFragment> expectedFragments, String ... args) {
        commandLineRunner.run(args);

        List<BookFragment> actualFragments = repository.findAll();
        actualFragments.forEach(fragment -> {
            fragment.setId(DUMMY_STRING);
            fragment.setNextFragmentId(DUMMY_STRING);
            fragment.setCreationTime(DUMMY_TIME);
        });

        assertTrue(expectedFragments.size() == actualFragments.size()
                && expectedFragments.containsAll(actualFragments));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    private static Stream<Arguments> provideTestData() throws FileNotFoundException {
        List<BookFragment> europeanLanguagesFragments = new ArrayList<>() {{
            add(new BookFragment(DUMMY_STRING, "The European Languages", DUMMY_TIME, "The European languages " +
                    "are members of the same family. Their separate existence is a myth. For science, music, sport, " +
                    "etc, Europe uses the same vocabulary. The languages only differ in their grammar, their " +
                    "pronunciation and their most common words. Everyone realizes why a new common language " +
                    "would be desirable: one could refuse to pay expensive translators.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Grammar", DUMMY_TIME, "To achieve this, it would be necessary to have " +
                    "uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar " +
                    "of the resulting language is more simple and regular than that of the individual languages. The new " +
                    "common language will be more simple and regular than the existing European languages.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Occidental", DUMMY_TIME, "It will be as simple as Occidental; " +
                    "in fact, it will be Occidental. To an English person, it will seem like simplified English, as " +
                    "a skeptical Cambridge friend of mine told me what Occidental is. The European languages are " +
                    "members of the same family. Their separate existence is a myth. For science, music, sport, etc, " +
                    "Europe uses the same vocabulary.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Pronunciation", DUMMY_TIME, "The languages only differ in their " +
                    "grammar, their pronunciation and their most common words. Everyone realizes why a new common " +
                    "language would be desirable: one could refuse to pay expensive translators. To achieve this, " +
                    "it would be necessary to have uniform grammar, pronunciation and more common words.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Coalesce between languages", DUMMY_TIME, "If several languages " +
                    "coalesce, the grammar of the resulting language is more simple and regular than that of the " +
                    "individual languages. The new common language will be more simple and regular than the existing " +
                    "European languages. It will be as simple as Occidental; in fact, it will be Occidental. To an " +
                    "English person, it will seem like simplified English, as a skeptical Cambridge friend of mine " +
                    "told me what Occidental is.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "The Family", DUMMY_TIME, "The European languages are members " +
                    "of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe " +
                    "uses the same vocabulary. The languages only differ in their grammar, their pronunciation and " +
                    "their most common words. Everyone realizes why a new common language would be desirable: one " +
                    "could refuse to pay expensive translators. To achieve this, it would be necessary to have " +
                    "uniform grammar, pronunciation and more common words. If several languages coalesce, the " +
                    "grammar of the resulting language is more simple and regular than that of the individual " +
                    "languages. The new common language will be more simple and regular than the existing European " +
                    "languages. It will be as simple as", DUMMY_STRING));
        }};

        List<BookFragment> farFarAwayFragments = new ArrayList<>() {{
            add(new BookFragment(DUMMY_STRING, "Introduction", DUMMY_TIME, "Far far away, behind the word " +
                    "mountains, far from the countries Vokalia and Consonantia, there live the blind texts. " +
                    "Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language " +
                    "ocean. A small river named Duden flows by their place and supplies it with the " +
                    "necessary regelialia.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "World of Grammar", DUMMY_TIME, "It is a paradisematic " +
                    "country, in which roasted parts of sentences fly into your mouth. Even the all-powerful " +
                    "Pointing has no control about the blind texts it is an almost unorthographic life One day " +
                    "however a small line of blind text by the name of Lorem Ipsum decided to leave for the far " +
                    "World of Grammar.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "The Big Oxmox", DUMMY_TIME, "The Big Oxmox advised her not " +
                    "to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, " +
                    "but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial " +
                    "into the belt and made herself on the way.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Italic Mountains", DUMMY_TIME, "When she reached the first " +
                    "hills of the Italic Mountains, she had a last view back on the skyline of her hometown " +
                    "Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line " +
                    "Lane. Pityful a rethoric question ran over her cheek, then she continued her way. On her way " +
                    "she met a copy.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Little Blind Text", DUMMY_TIME, "The copy warned the " +
                    "Little Blind Text, that where it came from it would have been rewritten a thousand times " +
                    "and everything that was left from its origin would be the word \"and\" and the Little Blind " +
                    "Text should turn around and return to its own, safe country. But nothing the copy said could " +
                    "convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, made " +
                    "her drunk with Longe and Parole and dragged her into their agency, where they abused her for " +
                    "their projects again and again.", DUMMY_STRING));

            add(new BookFragment(DUMMY_STRING, "Far Away", DUMMY_TIME, "And if she hasn’t been rewritten, " +
                    "then they are still using her. Far far away, behind the word mountains, far from the countries " +
                    "Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove " +
                    "right at the coast of the Semantics, a large language ocean. A small river named Duden flows " +
                    "by their place and supplies it with the necessary regelialia. It is a paradisematic country, " +
                    "in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no " +
                    "control about the blind texts it is an almost unorthographic life One", DUMMY_STRING));
        }};

        String europeanLanguagesDirectory = ResourceUtils.getFile("classpath:european-languages")
                .getAbsolutePath();

        File farFarAwayDirectory = ResourceUtils.getFile("classpath:far-far-away");
        String[] farFarAwayFiles = Arrays.stream(farFarAwayDirectory.listFiles())
                .map(File::getAbsolutePath)
                .toArray(String[]::new);
        String[] farFarAwayArgs = Stream.concat(Arrays.stream(new String[] { "parse", "-f" }), Arrays.stream(farFarAwayFiles))
                .toArray(String[]::new);


        return Stream.of(
                Arguments.of(europeanLanguagesFragments, new String[] { "parse", "-d", europeanLanguagesDirectory }),
                Arguments.of(farFarAwayFragments, farFarAwayArgs)
        );
    }
}
