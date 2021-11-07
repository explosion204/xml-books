package com.karnyshov.xmlbooks.service.parsing;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.service.dto.BookFragmentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.karnyshov.xmlbooks.service.parsing.XmlTagName.BODY;
import static com.karnyshov.xmlbooks.service.parsing.XmlTagName.CONTENT;
import static com.karnyshov.xmlbooks.service.parsing.XmlTagName.TITLE;

/**
 * This service class encapsulated business logic related to {@link BookFragment} parsing from XML.
 * A StAX parser is used as XML parser.
 */
@Service
public class ParsingService {
    private static final Logger logger = LoggerFactory.getLogger(ParsingService.class);
    private static final Pattern contentLinkPattern = Pattern.compile("<\\?content-link file=\"(.+)\"\\?>");

    /**
     * Parse XML files.
     *
     * @param fileNames names of XML files
     * @return list of {@link BookFragmentDtoNode} graph nodes.
     */
    public List<BookFragmentDtoNode> parseXml(List<String> fileNames) {
        // XML file name -> parsed node
        Map<String, BookFragmentDtoNode> nodeMap = new HashMap<>();

        // XML file name -> linked (next) XML file name
        Map<String, String> fileReferences = new HashMap<>();

        try {
            for (String fileName : fileNames) {
                parseBookFragment(fileName, fileReferences, nodeMap);
            }
        } catch (XMLStreamException e) {
            logger.error("Unable to parse book fragment. Cause: ", e);
        }

        // link parsed nodes
        nodeMap.forEach((fileName, node) -> {
            if (fileReferences.containsKey(fileName)) {
                String linkedFileName = fileReferences.get(fileName);

                if (nodeMap.containsKey(linkedFileName)) {
                    BookFragmentDtoNode linkedNode = nodeMap.get(linkedFileName);
                    node.setNextNode(linkedNode);
                }
            }
        });

        return nodeMap.values()
                .stream()
                .toList();
    }

    private void parseBookFragment(String filePath, Map<String, String> fileReferences, Map<String,
                BookFragmentDtoNode> nodeMap) throws XMLStreamException {

        BookFragmentDto fragmentDto = new BookFragmentDto();
        // we use just a file name (not absolute paths) to link nodes to each other
        String fileName = new File(filePath).getName();

        Source source = new StreamSource(filePath);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = inputFactory.createXMLEventReader(source);

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartElement()) {
                parseStartElement(reader, event, fragmentDto);
            }

            if (event.isProcessingInstruction()) {
                parseProcessingInstruction(event, fileName, fileReferences);
            }

            if (event.isEndElement()) {
                parseEndElement(event, fragmentDto, fileName, nodeMap);
            }
        }

        reader.close();
    }

    private void parseStartElement(XMLEventReader reader, XMLEvent event, BookFragmentDto fragmentDto)
                throws XMLStreamException {

        StartElement startElement = event.asStartElement();
        String tagName = startElement.getName().getLocalPart();

        switch (tagName) {
            case TITLE -> {
                event = reader.nextEvent();
                String title = event.asCharacters()
                        .getData()
                        .strip();
                fragmentDto.setTitle(title);
            }
            case BODY -> {
                event = reader.nextEvent();
                String body = event.asCharacters()
                        .getData()
                        .strip();
                fragmentDto.setBody(body);
            }
            default -> fragmentDto.setType(tagName);
        }
    }

    private void parseProcessingInstruction(XMLEvent event, String fileName, Map<String, String> fileReferences) {
        // parsing processing instruction regarding linked XML file
        String processingInstruction = event.toString();
        Matcher matcher = contentLinkPattern.matcher(processingInstruction);

        if (matcher.find()) {
            String linkedFileName = matcher.group(1);
            fileReferences.put(fileName, linkedFileName);
        }
    }

    private void parseEndElement(XMLEvent event, BookFragmentDto fragmentDto, String fileName,
                Map<String, BookFragmentDtoNode> nodeMap) {

        EndElement endElement = event.asEndElement();
        String tagName = endElement.getName().getLocalPart();

        if (tagName.equals(CONTENT)) {
            BookFragmentDtoNode node = new BookFragmentDtoNode();
            node.setBookFragmentDto(fragmentDto);
            nodeMap.put(fileName, node);
        }
    }
}
