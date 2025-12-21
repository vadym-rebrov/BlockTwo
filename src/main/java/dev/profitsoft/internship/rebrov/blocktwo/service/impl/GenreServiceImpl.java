package dev.profitsoft.internship.rebrov.blocktwo.service.impl;

import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreQueryDto;
import dev.profitsoft.internship.rebrov.blocktwo.repository.GenreRepository;
import dev.profitsoft.internship.rebrov.blocktwo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Override
    public List<GenreInfoDto> getGenresByQuery(GenreQueryDto dto) {
        return genreRepository.getByNameContains(dto.getName()).stream().map(GenreInfoDto::new).collect(Collectors.toList());
    }
}
