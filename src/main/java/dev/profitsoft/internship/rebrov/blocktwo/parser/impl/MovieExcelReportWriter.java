package dev.profitsoft.internship.rebrov.blocktwo.parser.impl;

import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.parser.ExcelReportWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class MovieExcelReportWriter implements ExcelReportWriter<Movie> {

    @Override
    public ByteArrayInputStream write(Stream<Movie> dataStream) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(500)) {
            Sheet sheet = workbook.createSheet("Movies");
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            createHeader(sheet, workbook);
            final AtomicInteger rowIndex = new AtomicInteger(1);
            try (Stream<Movie> movieStream = dataStream) {
                movieStream.forEach(movie -> {
                    Row row = sheet.createRow(rowIndex.getAndIncrement());
                    fillMovieRow(new MovieInfoDto(movie), row);
                });
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.dispose();
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    public void createHeader(Sheet sheet, Workbook workbook) {
        Row header = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        String[] columns = {
                "ID", "Title", "Released", "Genres", "Rating",
                "Director Name", "Director Country", "Birthday", "Awards"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell c = header.createCell(i);
            c.setCellValue(columns[i]);
            c.setCellStyle(style);
        }
    }

    public void fillMovieRow(MovieInfoDto m, Row row) {
        row.createCell(0).setCellValue(m.getId());
        row.createCell(1).setCellValue(m.getTitle());
        row.createCell(2).setCellValue(m.getReleased() != null ? m.getReleased().toString() : "");

        row.createCell(3).setCellValue(
                m.getGenres() != null
                        ? m.getGenres().stream()
                        .map(Genre::getName)
                        .sorted()
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
                        : ""
        );

        row.createCell(4).setCellValue(m.getRating() != null ? m.getRating() : 0.0);

        DirectorInfoDto d = m.getDirector();
        row.createCell(5).setCellValue(d != null ? d.getFullName() : "");
        row.createCell(6).setCellValue(d != null ? d.getCountry().getName() : "");
        row.createCell(7).setCellValue(
                d != null && d.getBirthday() != null
                        ? d.getBirthday().toString()
                        : ""
        );

        row.createCell(8).setCellValue(
                m.getAwards() != null
                        ? String.join(", ", m.getAwards())
                        : ""
        );
    }
}
