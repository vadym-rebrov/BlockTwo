package dev.profitsoft.internship.rebrov.blocktwo.service.impl;

import dev.profitsoft.internship.rebrov.blocktwo.data.Country;
import dev.profitsoft.internship.rebrov.blocktwo.data.Director;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorQueryDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorSaveDto;
import dev.profitsoft.internship.rebrov.blocktwo.repository.CountryRepository;
import dev.profitsoft.internship.rebrov.blocktwo.repository.DirectorRepository;
import dev.profitsoft.internship.rebrov.blocktwo.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private CountryRepository countryRepository;

    /**
     * Retrieves a list of all directors currently stored in the database.
     *
     * @return A list of DTOs representing all directors.
     */
    @Override
    public List<DirectorInfoDto> getAll() {
        return directorRepository.getAll().stream().map(DirectorInfoDto::new).collect(Collectors.toList());
    }

    /**
     * Retrieves the details of a specific director by their unique identifier.
     *
     * @param id The ID of the director to retrieve.
     * @return DTO containing the director's information.
     */
    @Override
    public DirectorInfoDto getById(Long id) {
        return directorRepository.getById(id).map(DirectorInfoDto::new).orElseThrow();
    }

    /**
     * Creates and saves a new director record in the database.
     *
     * @param obj DTO containing the data for the new director.
     */
    @Override
    public void save(DirectorSaveDto obj) {
        Country country = countryRepository.getById(obj.getCountryId()).orElseThrow(()-> new NoSuchElementException("Country not found"));
        Director director = Director.builder()
                .fullName(obj.getFullName())
                .birthday(obj.getBirthday())
                .country(country)
                .build();
        directorRepository.save(director);
    }

    /**
     * Updates the details of an existing director identified by their unique identifier.
     *
     * @param id The ID of the director to update.
     * @param obj DTO containing the updated data.
     */
    @Override
    public void update(Long id, DirectorSaveDto obj) {
        Director director = directorRepository.getById(id).orElseThrow(()-> new NoSuchElementException("Director not found"));
        Country country = countryRepository.getById(obj.getCountryId()).orElseThrow(()-> new NoSuchElementException("Country not found"));
        director.setFullName(obj.getFullName());
        director.setBirthday(obj.getBirthday());
        director.setCountry(country);
        directorRepository.update(director);
    }

    /**
     * Removes a director record from the database by their unique identifier.
     *
     * @param id The ID of the director to delete.
     */
    @Override
    public void delete(Long id) {
        directorRepository.delete(id);
    }

    @Override
    public List<DirectorInfoDto> getByNameContains(DirectorQueryDto dto) {
        return directorRepository.findByFullName(dto.getName()).stream().map(DirectorInfoDto::new).collect(Collectors.toList());
    }


}
