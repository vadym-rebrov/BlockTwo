package dev.profitsoft.internship.rebrov.blocktwo.data;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "movie")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private LocalDate released;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;
    @Column(nullable = false)
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Director director;

    @ElementCollection
    @CollectionTable(
            name = "movie_awards",
            joinColumns = @JoinColumn(name = "movie_id")
    )
    @Column(name = "award_name")
    private List<String> awards;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Movie movie)) return false;
        return Objects.equals(title, movie.title) && Objects.equals(released, movie.released) && Objects.equals(genres, movie.genres) && Objects.equals(rating, movie.rating) && Objects.equals(director, movie.director) && Objects.equals(awards, movie.awards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, released, genres, rating, director, awards);
    }
}

