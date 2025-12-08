package dev.profitsoft.internship.rebrov.blocktwo.parser;

import java.io.ByteArrayInputStream;
import java.util.stream.Stream;

public interface ExcelReportWriter<T> {
    ByteArrayInputStream write(Stream<T> dataStream);
}
