package dev.profitsoft.internship.rebrov.blocktwo.util;

import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieInfoDto;
import org.apache.poi.ss.usermodel.*;

public class MovieExcelUtils {

    public static void createHeader(Sheet sheet, Workbook workbook) {
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

    public static void fillMovieRow(MovieInfoDto m, Row row) {
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
