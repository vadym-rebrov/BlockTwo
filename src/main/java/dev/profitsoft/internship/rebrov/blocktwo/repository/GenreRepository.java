package dev.profitsoft.internship.rebrov.blocktwo.repository;

import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends AbstractJpaRepository<Genre>{
    public Optional<Genre> getByName(String name){
        String query = "SELECT g FROM Genre g WHERE g.name = :name";
        return Optional.ofNullable(entityManager.createQuery(query, Genre.class)
                .setParameter("name", name).getSingleResult());

    }

    public List<Genre> getByNameContains(String name) {
        name = name.trim();
        String query = "SELECT g FROM Genre g WHERE g.name ILIKE :name";
        return entityManager.createQuery(query, Genre.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

}
