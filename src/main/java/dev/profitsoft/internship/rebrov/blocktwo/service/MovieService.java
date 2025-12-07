package dev.profitsoft.internship.rebrov.blocktwo.service;
import dev.profitsoft.internship.rebrov.blocktwo.dto.*;
import jakarta.persistence.EntityExistsException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

public interface MovieService{
    List<MovieDetailsDto> findMoviesByCriteria(MovieQueryDto parameters);
    MovieInfoDto getById(Long id);
    void deleteById(Long id);
    void add(MovieSaveDto dto) throws NoSuchElementException, EntityExistsException;
    void update(Long id, MovieSaveDto dto) throws NoSuchElementException;
    FileReportResponseDto getExcelReportByCriteria(MovieQueryDto parameters);
    UploadReport uploadMultipartFile(MultipartFile file);

}
