package dev.profitsoft.internship.rebrov.blocktwo.repository;

import dev.profitsoft.internship.rebrov.blocktwo.data.Country;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class DirectorRepository extends AbstractJpaRepository<Director> {

    public Optional<Director> findByFullName(@NotNull String name) {
        String preparedQuery = "SELECT d FROM Director d WHERE fts('english', d.fullName, :searchName) = TRUE";

        TypedQuery<Director> query = entityManager.createQuery(preparedQuery, Director.class);
        query.setParameter("searchName", name.trim());
        query.setMaxResults(1);

        return query.getResultStream().findFirst();
    }

    public Optional<Director> getExist(@NotNull String name,
                           @NotNull LocalDate birthday,
                           @NotNull Country country){
        String preparedQuery = "SELECT d FROM Director d WHERE d.fullName = :name and d.birthday = :birthday and d.country = :country";
        TypedQuery<Director> query = entityManager.createQuery(preparedQuery, Director.class);
        query.setParameter("name", name);
        query.setParameter("birthday", birthday);
        query.setParameter("country", country);
        query.setMaxResults(1);

        return query.getResultStream().findFirst();
    }

}