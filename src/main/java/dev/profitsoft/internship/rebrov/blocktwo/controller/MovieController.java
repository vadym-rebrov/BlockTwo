package dev.profitsoft.internship.rebrov.blocktwo.controller;

import dev.profitsoft.internship.rebrov.blocktwo.dto.*;
import dev.profitsoft.internship.rebrov.blocktwo.service.MovieService;
import dev.profitsoft.internship.rebrov.blocktwo.service.impl.MovieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    /**
     * Creates a new movie entry.
     *
     * @param movieDto DTO containing movie data. Fields are validated via annotations (e.g., @NotBlank, @Size).
     * @return ResponseEntity with status 200 OK upon success.
     */
    @PostMapping
    public ResponseEntity<?> addMovie(@Valid @RequestBody MovieSaveDto movieDto){
        movieService.add(movieDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Updates an existing movie by its ID.
     *
     * @param id ID of the movie to update (from the URL path).
     * @param movieDto DTO containing new data.
     * @return ResponseEntity with status 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieSaveDto movieDto){
        movieService.update(id, movieDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes a movie by the specified ID.
     *
     * @param id ID of the movie to delete.
     * @return ResponseEntity with status 200 OK.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        movieService.deleteById(id);
        return ResponseEntity.ok().build();

    }

    /**
     * Retrieves detailed information about a movie.
     *
     * @param id ID of the movie.
     * @return DTO containing full movie information, including director details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovieInfoDto> getMovie(@PathVariable Long id){
        return ResponseEntity.ok(movieService.getById(id));
    }

    /**
     * Retrieves a list of movies with pagination and filtering support.
     * Filtering is possible by country, director name (Full Text Search), rating, and release year.
     *
     * @param queryDto Object containing filtering and pagination parameters (page, size).
     * @return List of movie DTOs (abbreviated information for lists).
     */
    @PostMapping("/_list")
    public List<MovieDetailsDto> getMovieList(@Valid @RequestBody MovieQueryDto queryDto){
        return movieService.findMoviesByCriteria(queryDto);
    }

    /**
     * Generates and returns an Excel report (.xlsx) with the list of movies
     * matching the filter criteria.
     *
     * @param queryDto Filter criteria for data selection.
     * @return ResponseEntity containing the file in the body and a Content-Disposition header for download.
     */
    @PostMapping("/_report")
    public ResponseEntity<Resource> exportMoviesReportByQuery(@Valid @RequestBody MovieQueryDto queryDto){
        FileReportResponseDto excelReport = movieService.getExcelReportByCriteria(queryDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(excelReport.getFilename()).build());
        InputStreamResource file = new InputStreamResource(excelReport.getFile());
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    /**
     * Uploads a JSON file containing movies and saves them to the DB.
     * Processes the file via streaming, saving valid entries and skipping erroneous ones.
     *
     * @param file The uploaded file (MultipartFile).
     * @return UploadReport containing the count of successfully saved and failed entries.
     */
    @PostMapping(value = "/_upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadReport> uploadMovies(@RequestParam("file") MultipartFile file){
        UploadReport report = movieService.uploadMultipartFile(file);
        return ResponseEntity.ok(report);
    }

}
