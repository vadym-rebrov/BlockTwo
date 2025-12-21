package dev.profitsoft.internship.rebrov.blocktwo.controller;

import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreInfoDto;
import dev.profitsoft.internship.rebrov.blocktwo.dto.GenreQueryDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


import dev.profitsoft.internship.rebrov.blocktwo.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

    @Autowired
    private GenreService genreService;


    @PostMapping("/_list")
    public ResponseEntity<List<GenreInfoDto>> getGenresByNameContains(@RequestBody @Valid GenreQueryDto queryDto){
        System.out.println(queryDto.getName());
        return ResponseEntity.ok(genreService.getGenresByQuery(queryDto));
    }
}
