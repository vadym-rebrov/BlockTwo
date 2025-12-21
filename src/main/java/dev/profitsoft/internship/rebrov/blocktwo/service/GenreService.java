package dev.profitsoft.internship.rebrov.blocktwo.service;

import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreQueryDto;

import java.util.List;

public interface GenreService {
    List<GenreInfoDto> getGenresByQuery(GenreQueryDto dto);
}
