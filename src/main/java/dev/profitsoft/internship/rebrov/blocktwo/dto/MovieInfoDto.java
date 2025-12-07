package dev.profitsoft.internship.rebrov.blocktwo.dto;

import dev.profitsoft.internship.rebrov.blocktwo.data.Country;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class MovieInfoDto{
    private Long id;
    private String title;
    private LocalDate released;
    private Set<Genre> genres;
    private Double rating;
    private DirectorInfoDto director;
    private List<String> awards;

    public MovieInfoDto(Movie movie){
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.released = movie.getReleased();
        this.genres = movie.getGenres();
        this.rating = movie.getRating();
        Director dir = movie.getDirector();
        Country country = dir.getCountry();
        this.director = new DirectorInfoDto(
                dir.getId(),
                dir.getFullName(),
                new CountryInfoDto(country.getId(), country.getName()),
                dir.getBirthday());
        this.awards = movie.getAwards();
    }
}
