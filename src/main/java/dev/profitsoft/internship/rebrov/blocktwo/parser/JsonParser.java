package dev.profitsoft.internship.rebrov.blocktwo.parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;


public class JsonParser<T> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> type;

    public JsonParser(Class<T> type) {
        this.type = type;
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void readAndProcess(InputStream inputStream, Consumer<T> processor) {
        try (MappingIterator<T> iterator =
                     mapper.readerFor(type).readValues(inputStream)) {

            while (iterator.hasNext()) {
                T obj = iterator.next();
                processor.accept(obj);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error processing JSON stream", e);
        }
    }

    public void writeFile(List<T> objList, String filename) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), objList);
        } catch (IOException e) {
            System.err.println("Error writing file " + filename + ": " + e.getMessage());
        }
    }
}