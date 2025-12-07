package dev.profitsoft.internship.rebrov.blocktwo.repository;

import dev.profitsoft.internship.rebrov.blocktwo.data.Country;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CountryRepository extends AbstractJpaRepository<Country>{
    public Optional<Country> findByName(String name){
        String query = "SELECT c FROM Country c WHERE c.name = :name";
        return Optional.ofNullable(entityManager.createQuery(query, Country.class)
                .setParameter("name", name).getSingleResult());

    }

}
