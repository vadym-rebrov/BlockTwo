package dev.profitsoft.internship.rebrov.blocktwo.dto;

import dev.profitsoft.internship.rebrov.blocktwo.data.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenreInfoDto {
    private Long id;
    private String name;

    public GenreInfoDto(Genre genre){
        this.id = genre.getId();
        this.name = genre.getName();
    }
}
