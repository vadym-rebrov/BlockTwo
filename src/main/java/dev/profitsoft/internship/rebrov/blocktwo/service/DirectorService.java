package dev.profitsoft.internship.rebrov.blocktwo.service;

import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorQueryDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorSaveDto;

import java.util.List;


public interface DirectorService {
    List<DirectorInfoDto> getAll();
    DirectorInfoDto getById(Long id);
    void save(DirectorSaveDto obj);
    void update(Long id, DirectorSaveDto obj);
    void delete(Long id);

    List<DirectorInfoDto> getByNameContains(DirectorQueryDto dto);
}
