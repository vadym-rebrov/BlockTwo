package dev.profitsoft.internship.rebrov.blocktwo.data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class Movie {
    private Integer id;
    private String title;
    private Integer releaseYear;
    private String genres;
    private Double rating;
    private Director director;
    private List<String> awards;

    public Movie(String title, Integer releaseYear, String genres, Double rating, Director director, List<String> awards) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.rating = rating;
        this.director = director;
        this.awards = awards;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Movie movie)) return false;
        return Objects.equals(title, movie.title) && Objects.equals(releaseYear, movie.releaseYear) && Objects.equals(genres, movie.genres) && Objects.equals(rating, movie.rating) && Objects.equals(director, movie.director) && Objects.equals(awards, movie.awards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, releaseYear, genres, rating, director, awards);
    }
}

