package com.karnyshov.xmlbooks.service.parsing;

import com.karnyshov.xmlbooks.service.BookFragmentService;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ParseCommandLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ParseCommandLineRunner.class);
    private static final String PARSE_ARG = "parse";
    private static final String FILE_PARAM = "-f";
    private static final String DIRECTORY_PARAM = "-d";

    private final ParsingService parsingService;
    private final BookFragmentService fragmentService;

    public ParseCommandLineRunner(ParsingService parsingService, BookFragmentService fragmentService) {
        this.parsingService = parsingService;
        this.fragmentService = fragmentService;
    }

    @Override
    public void run(String ... args) {
        if (args.length > 2 && args[0].equals(PARSE_ARG)) {
            List<String> fileNames = extractFileNames(args);

            if (fileNames != null) {
                parsingService.parseXml(fileNames)
                        .forEach(node -> {
                            if (!node.isSaved()) {
                                persistNode(node);
                            }
                        });

            }
        }
    }

    private List<String> extractFileNames(String ... args) {
        List<String> fileNames = new ArrayList<>();

        if (args[1].equals(FILE_PARAM)) {
            fileNames = Arrays.stream(args)
                    .skip(2)
                    .toList();
        } else if (args[1].equals(DIRECTORY_PARAM)) {
            String directoryPath = args[2];
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();

            if (files != null) {
                fileNames = Arrays.stream(files)
                        .map(File::getAbsolutePath)
                        .toList();
            } else {
                logger.error("Unable to read data from directory: {}", directoryPath);
            }
        } else {
            logger.error("Invalid parse parameters");
        }

        return fileNames;
    }

    private void persistNode(BookFragmentDtoNode node) {
        BookFragmentDtoNode nextNode = node.getNextNode();

        if (nextNode != null && !nextNode.isSaved()) {
            persistNode(nextNode);
        }

        BookFragmentDto fragmentDto = node.getBookFragmentDto();
        String nextFragmentId = nextNode != null ? nextNode.getId() : null;
        fragmentDto.setNextFragmentId(nextFragmentId);

        String id = fragmentService.save(fragmentDto);
        node.setId(id);
        node.setSaved(true);
    }
}
