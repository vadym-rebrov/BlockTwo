package dev.profitsoft.internship.rebrov.blocktwo.repository;

import dev.profitsoft.internship.rebrov.blocktwo.data.Country;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MovieQueryDto;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class MovieRepository extends AbstractJpaRepository<Movie> {

    public List<Movie> findMoviesByCriteria(MovieQueryDto parameters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);

        Root<Movie> movie = cq.from(Movie.class);

        Join<Movie, Director> director = movie.join("director");
        Join<Director, Country> country = director.join("country");

        List<Predicate> predicates = new LinkedList<>();
        if (parameters.getCountry() != null && !parameters.getCountry().isBlank()) {
            Predicate countryPredicate = cb.equal(country.get("name"), parameters.getCountry());
            predicates.add(countryPredicate);
        }

        if (parameters.getDirectorName() != null && !parameters.getDirectorName().isBlank()) {

            String searchTerm = parameters.getDirectorName().trim();

            Expression<String> config = cb.literal("english");
            Expression<String> field = director.get("fullName");
            Expression<String> query = cb.literal(searchTerm);
            Expression<Boolean> ftsExpression = cb.function(
                    "fts",
                    Boolean.class,
                    config,
                    field,
                    query
            );

            Predicate fullTextPredicate = cb.isTrue(ftsExpression);
            predicates.add(fullTextPredicate);
        }

        if (parameters.getMinRating() != null) {
            Predicate minRatingPredicate = cb.greaterThanOrEqualTo(movie.get("rating"), parameters.getMinRating());
            predicates.add(minRatingPredicate);
        }
        if (parameters.getMaxRating() != null) {
            Predicate maxRatingPredicate = cb.lessThanOrEqualTo(movie.get("rating"), parameters.getMaxRating());
            predicates.add(maxRatingPredicate);
        }

        if (parameters.getMinYear() != null) {
            Expression<Integer> releaseYear = cb.function("YEAR", Integer.class, movie.get("released"));
            Predicate minYearPredicate = cb.greaterThanOrEqualTo(releaseYear, parameters.getMinYear());
            predicates.add(minYearPredicate);
        }
        if (parameters.getMaxYear() != null) {
            Expression<Integer> releaseYear = cb.function("YEAR", Integer.class, movie.get("released"));
            Predicate maxYearPredicate = cb.lessThanOrEqualTo(releaseYear, parameters.getMaxYear());
            predicates.add(maxYearPredicate);
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Movie> query = entityManager.createQuery(cq);

        query.setFirstResult(parameters.getPage() * parameters.getSize());
        query.setMaxResults(parameters.getSize());

        return query.getResultList();
    }

    public Optional<Movie> getExist(String title, LocalDate released, Double rating, Director director) {
        String query = "SELECT m FROM Movie m WHERE m.title = :title AND m.released = :released AND m.rating = :rating AND m.director = :director";

        List<Movie> list = entityManager.createQuery(query, Movie.class)
                .setParameter("title", title)
                .setParameter("released", released)
                .setParameter("rating", rating)
                .setParameter("director", director)
                .getResultList();
        return list.stream().findFirst();
    }


}
