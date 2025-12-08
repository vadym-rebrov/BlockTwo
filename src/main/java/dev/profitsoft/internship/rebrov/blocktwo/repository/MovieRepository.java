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
import java.util.stream.Stream;

@Repository
public class MovieRepository extends AbstractJpaRepository<Movie> {

    private TypedQuery<Movie> findMoviesByCriteria(MovieQueryDto parameters) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
        Root<Movie> movie = cq.from(Movie.class);

        List<Predicate> predicates = buildPredicates(cb, movie, parameters);

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Movie> query = entityManager.createQuery(cq);
        return query;
    }

    public List<Movie> findPageMoviesByCriteria(MovieQueryDto parameters) {
        TypedQuery<Movie> query = findMoviesByCriteria(parameters);
        query.setFirstResult(parameters.getPage() * parameters.getSize());
        query.setMaxResults(parameters.getSize());
        return  query.getResultList();
    }

    public Stream<Movie> streamMoviesByCriteria(MovieQueryDto parameters) {
        TypedQuery<Movie> query = findMoviesByCriteria(parameters);
        return query.getResultStream();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Movie> movie, MovieQueryDto parameters) {
        // Важливо: Джойни потрібно робити тут або передавати їх сюди
        Join<Movie, Director> director = movie.join("director");
        Join<Director, Country> country = director.join("country");

        List<Predicate> predicates = new LinkedList<>();

        if (parameters.getCountry() != null && !parameters.getCountry().isBlank()) {
            predicates.add(cb.equal(country.get("name"), parameters.getCountry()));
        }

        if (parameters.getDirectorName() != null && !parameters.getDirectorName().isBlank()) {
            String searchTerm = parameters.getDirectorName().trim();
            predicates.add(cb.isTrue(cb.function("fts", Boolean.class,
                    cb.literal("english"),
                    director.get("fullName"),
                    cb.literal(searchTerm))));
        }

        if (parameters.getMinRating() != null) {
            predicates.add(cb.greaterThanOrEqualTo(movie.get("rating"), parameters.getMinRating()));
        }
        if (parameters.getMaxRating() != null) {
            predicates.add(cb.lessThanOrEqualTo(movie.get("rating"), parameters.getMaxRating()));
        }

        if (parameters.getMinYear() != null) {
            Expression<Integer> releaseYear = cb.function("YEAR", Integer.class, movie.get("released"));
            predicates.add(cb.greaterThanOrEqualTo(releaseYear, parameters.getMinYear()));
        }
        if (parameters.getMaxYear() != null) {
            Expression<Integer> releaseYear = cb.function("YEAR", Integer.class, movie.get("released"));
            predicates.add(cb.lessThanOrEqualTo(releaseYear, parameters.getMaxYear()));
        }

        return predicates;
    }

    public Long getMoviesCount(MovieQueryDto parameters) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Movie> movie = cq.from(Movie.class);

        List<Predicate> predicates = buildPredicates(cb, movie, parameters);

        cq.select(cb.count(movie));

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(cq).getSingleResult();
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
