package dev.profitsoft.internship.rebrov.blocktwo.service.impl;

import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import dev.profitsoft.internship.rebrov.blocktwo.dto.*;
import dev.profitsoft.internship.rebrov.blocktwo.parser.JsonParser;
import dev.profitsoft.internship.rebrov.blocktwo.repository.DirectorRepository;
import dev.profitsoft.internship.rebrov.blocktwo.repository.GenreRepository;
import dev.profitsoft.internship.rebrov.blocktwo.repository.MovieRepository;
import dev.profitsoft.internship.rebrov.blocktwo.service.MovieService;
import dev.profitsoft.internship.rebrov.blocktwo.util.MovieExcelUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService{
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private Validator validator;

    /**
     * Retrieves a list of movies based on the provided filtering and pagination criteria.
     *
     * @param parameters DTO containing filter values (country, director, year, rating) and pagination settings.
     * @return A list of movie details DTOs matching the criteria.
     */
    @Override
    public List<MovieDetailsDto> findMoviesByCriteria(MovieQueryDto parameters) {
        return movieRepository.findMoviesByCriteria(parameters).stream().map(MovieDetailsDto::new).toList();
    }

    /**
     * Retrieves the details of a specific movie by its unique identifier.
     *
     * @param id The ID of the movie to retrieve.
     * @return DTO containing full details of the found movie.
     */
    @Override
    public MovieInfoDto getById(Long id) throws NoSuchElementException {
        return new MovieInfoDto(movieRepository.getById(id).orElseThrow(()-> new NoSuchElementException("Movie not found")));
    }

    /**
     * Removes a movie record from the database by its unique identifier.
     *
     * @param id The ID of the movie to delete.
     */
    @Override
    public void deleteById(Long id) {
        movieRepository.delete(id);
    }

    /**
     * Creates and saves a new movie record in the database.
     * Maps the DTO to the entity and handles related entities like Director and Genres.
     *
     * @param dto DTO containing the data for the new movie.
     */
    @Override
    public void add(MovieSaveDto dto) {
        Movie movie = new Movie();
        mapDtoToMovie(movie, dto);
        movieRepository.save(movie);
    }

    /**
     * Updates the details of an existing movie identified by its unique identifier.
     *
     * @param id The ID of the movie to update.
     * @param dto DTO containing the updated data.
     */
    @Override
    public void update(Long id, MovieSaveDto dto) {
        Movie movie = movieRepository.getById(id).orElseThrow(()-> new NoSuchElementException("Movie not found"));
        mapDtoToMovie(movie, dto);
        movieRepository.update(movie);
    }

    private void mapDtoToMovie(Movie movie, MovieSaveDto dto) {
        if (movie.getDirector() == null || !movie.getDirector().getId().equals(dto.getDirectorId())) {
            Director director = directorRepository
                    .getById(dto.getDirectorId())
                    .orElseThrow(() -> new NoSuchElementException("Director not found"));
            movie.setDirector(director);
        }
        Set<Long> dtoGenres = dto.getGenresId();
        Set<Long> currentGenres = movie.getGenres() == null
                ? Collections.emptySet()
                : movie.getGenres().stream().map(Genre::getId).collect(Collectors.toSet());
        if (!currentGenres.equals(dtoGenres)) {
            List<Genre> loaded = genreRepository.getAllByIdSet(dtoGenres);
            if (loaded.size() != dtoGenres.size()) {
                throw new NoSuchElementException("Genres not found");
            }
            movie.setGenres(new HashSet<>(loaded));
        }
        movie.setTitle(dto.getTitle());
        movie.setReleased(dto.getReleased());
        movie.setRating(dto.getRating());
        movie.setAwards(dto.getAwards());
    }


    /**
     * Generates a byte array representing an Excel report (.xlsx) containing movies
     * that match the specified filter criteria.
     *
     * @param parameters DTO containing filter values to select the data for the report.
     * @return A byte array of the generated Excel file.
     */
    @Override
    public FileReportResponseDto getExcelReportByCriteria(MovieQueryDto parameters){
        int page = 0;
        int size = 500;
        parameters.setSize(size);
        parameters.setPage(page);

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(500)) {
            Sheet sheet = workbook.createSheet("Movies");
            ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            MovieExcelUtils.createHeader(sheet, workbook);
            final int[] rowIndex = {1};
            Consumer<MovieInfoDto> excelWriter = dto -> {
                synchronized (sheet) {
                    Row row = sheet.createRow(rowIndex[0]++);
                    MovieExcelUtils.fillMovieRow(dto, row);
                }
            };

            while (true) {
                parameters.setPage(page++);
                List<Movie> movies = movieRepository.findMoviesByCriteria(parameters);
                if (movies.isEmpty()){
                    break;
                }
                movies.stream()
                        .map(MovieInfoDto::new)
                        .forEach(excelWriter);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.dispose();
            return new FileReportResponseDto("movies_report.xlsx", new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }

    }

    /**
     * Processes an uploaded JSON file to import movie data into the database.
     * Reads the file in batches to manage memory usage efficiently.
     *
     * @param file The multipart file containing movie data in JSON format.
     * @return A report indicating the number of successfully imported and failed records.
     */
    @Override
    public UploadReport uploadMultipartFile(MultipartFile file){

        JsonParser<MovieSaveDto> parser = new JsonParser<>(MovieSaveDto.class);

        final int BATCH_SIZE = 500;
        List<MovieSaveDto> batch = new ArrayList<>(BATCH_SIZE);

        UploadReport report = new UploadReport();
        InputStream input;
        try{
            input = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        parser.readAndProcess(input, dto -> {
            batch.add(dto);
            if (batch.size() >= BATCH_SIZE) {
                mergeReports(report, mapAndSave(batch));
                batch.clear();
            }
        });

        if (!batch.isEmpty()) {
            mergeReports(report, mapAndSave(batch));
        }

        return report;
    }

    private void mergeReports(UploadReport main, UploadReport partial) {
        main.setSuccess(main.getSuccess() + partial.getSuccess());
        main.setFailed(main.getFailed() + partial.getFailed());
    }

    private UploadReport mapAndSave(List<MovieSaveDto> batch) {

        UploadReport report = new UploadReport();

        Set<Long> genreIds = batch.stream()
                .flatMap(dto -> dto.getGenresId().stream())
                .collect(Collectors.toSet());

        Set<Long> directorIds = batch.stream()
                .map(MovieSaveDto::getDirectorId)
                .collect(Collectors.toSet());

        Map<Long, Genre> genres = genreRepository.getAllByIdSet(genreIds)
                .stream().collect(Collectors.toMap(Genre::getId, g -> g));

        Map<Long, Director> directors = directorRepository.getAllByIdSet(directorIds)
                .stream().collect(Collectors.toMap(Director::getId, d -> d));

        List<Movie> movies = batch.stream().map(dto -> {
            try {
                validateMovieDto(dto, genres, directors);

                Movie movie = new Movie();
                movie.setTitle(dto.getTitle());
                movie.setReleased(dto.getReleased());
                movie.setRating(dto.getRating());
                movie.setAwards(dto.getAwards());
                movie.setDirector(directors.get(dto.getDirectorId()));

                movie.setGenres(dto.getGenresId()
                        .stream()
                        .map(genres::get)
                        .collect(Collectors.toSet()));

                report.setSuccess(report.getSuccess() + 1);
                return movie;

            } catch (Exception e) {
                report.setFailed(report.getFailed() + 1);
                return null;
            }
        }).filter(Objects::nonNull).toList();

        if (!movies.isEmpty()) {
            try {
                movieRepository.saveBatch(movies);
            } catch (Exception e) {
                report.setFailed(report.getFailed() + movies.size());
                report.setSuccess(report.getSuccess() - movies.size());
            }
        }

        return report;
    }


    private void validateMovieDto(MovieSaveDto dto, Map<Long, Genre> genres, Map<Long, Director> directors) {

        Set<ConstraintViolation<MovieSaveDto>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if (!directors.containsKey(dto.getDirectorId())) {
            throw new EntityNotFoundException("Director not found: " + dto.getDirectorId());
        }

        for (Long genreId : dto.getGenresId()) {
            if (!genres.containsKey(genreId)) {
                throw new EntityNotFoundException("Genre not found: " + genreId);
            }
        }
    }

}
