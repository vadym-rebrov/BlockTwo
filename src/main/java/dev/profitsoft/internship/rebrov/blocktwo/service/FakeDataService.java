package dev.profitsoft.internship.rebrov.blocktwo.service;

import com.github.javafaker.Faker;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieSaveDto;
import dev.profitsoft.internship.rebrov.blocktwo.parser.JsonParser;
import dev.profitsoft.internship.rebrov.blocktwo.repository.DirectorRepository;
import dev.profitsoft.internship.rebrov.blocktwo.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FakeDataService {

    @Autowired
    private DirectorRepository directorRepository;
    @Autowired
    private GenreRepository genreRepository;


    private static final Faker FAKER = new Faker();
    private static final String[] commonAwards = new String[]{
            "BAFTA", "Oscar", "Palme d’Or", "Golden Globe Award"
    };

    /**
     * Generates a JSON file containing fake movie data for testing or population purposes.
     * Uses existing genres and directors from the database to create valid relationships.
     *
     * @param numberOfMovies The number of fake movie records to generate.
     * @param filename The path and name of the file where the JSON data will be written.
     */
    public void generateFakeMoviesJson(int numberOfMovies, String filename) {
        List<MovieSaveDto> movies = new ArrayList<>();
        List<Genre> allGenres = genreRepository.getAll();
        List<Director> allDirectors = directorRepository.getAll();

        if (allGenres.isEmpty() || allDirectors.isEmpty()) {
            throw new IllegalStateException("Cannot generate fake data: Director or Genre repositories are empty.");
        }

        for (int i = 0; i < numberOfMovies; i++) {
            Director director = allDirectors.get(FAKER.number().numberBetween(0, allDirectors.size()));
            movies.add(generateFakeMovie(allGenres, director));
        }

        JsonParser<MovieSaveDto> parser = new JsonParser<>(MovieSaveDto.class);
        parser.writeFile(movies, filename);
    }

    public MovieSaveDto generateFakeMovie(List<Genre> genres, Director director) {
        String title = FAKER.book().title();
        LocalDate date = FAKER.date()
                .between(
                        java.sql.Date.valueOf(LocalDate.of(1900, 1, 1)),
                        java.sql.Date.valueOf(LocalDate.of(2025, 1, 1))
                )
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        int randomGenresQuantity = FAKER.number().numberBetween(1, Math.min(5, genres.size()));
        Set<Long> genresId = genres.stream()
                .limit(randomGenresQuantity)
                .map(Genre::getId)
                .collect(Collectors.toSet());

        double rating = FAKER.number().randomDouble(1, 1, 10);
        Long directorId = director.getId();
        List<String> awards = generateFakeAwards(FAKER.number().numberBetween(1, commonAwards.length));
        return new MovieSaveDto(title, date, genresId, rating, directorId, awards);
    }

    public List<String> generateFakeAwards(int numberOfAwards) {
        List<String> awards = new ArrayList<>();
        List<String> pool = new ArrayList<>(Arrays.asList(commonAwards));
        Collections.shuffle(pool);
        int limit = Math.min(numberOfAwards, pool.size());
        for (int i = 0; i < limit; i++) {
            awards.add((pool.get(i)));
        }

        return awards;
    }

}