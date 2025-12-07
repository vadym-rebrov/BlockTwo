package dev.profitsoft.internship.rebrov.blocktwo.dto;

import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDetailsDto{
    private Long id;
    private String title;
    private LocalDate released;
    private List<String> genres;
    private Double rating;
    private String directorFullName;
    private List<String> awards;

    public MovieDetailsDto(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.released = movie.getReleased();
        this.genres = movie.getGenres().stream().map(Genre::getName).toList();
        this.rating = movie.getRating();
        this.directorFullName = movie.getDirector().getFullName();
        this.awards = movie.getAwards();
    }
}
