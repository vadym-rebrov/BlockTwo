package dev.profitsoft.internship.rebrov.blocktwo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;

@Getter
@Setter
@AllArgsConstructor
public class FileReportResponseDto {
    private String filename;
    private ByteArrayInputStream file;

}
