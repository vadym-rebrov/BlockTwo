package dev.profitsoft.internship.rebrov.blocktwo.controller;

import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.DirectorSaveDto;
import dev.profitsoft.internship.rebrov.blocktwo.service.DirectorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/director")
public class DirectorController {

    @Autowired
    DirectorService directorService;

    /**
     * Retrieves a list of all directors present in the database.
     *
     * @return ResponseEntity containing a list of director DTOs.
     */
    @GetMapping
    public ResponseEntity<List<DirectorInfoDto>> getAll(){
        return ResponseEntity.ok(directorService.getAll());
    }

    /**
     * Retrieves director information by their ID.
     *
     * @param id ID of the director.
     * @return ResponseEntity containing the director DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectorInfoDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(directorService.getById(id));
    }

    /**
     * Deletes a director by ID.
     *
     * @param id ID of the director.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        directorService.delete(id);
    }

    /**
     * Creates a new director.
     *
     * @param dto Data for the new director (name, birthday, country ID).
     */
    @PostMapping
    public void save(@Valid @RequestBody DirectorSaveDto dto){
        directorService.save(dto);
    }

    /**
     * Updates an existing director's data.
     *
     * @param id ID of the director.
     * @param dto New data..
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody DirectorSaveDto dto){
        directorService.update(id, dto);
    }


}
