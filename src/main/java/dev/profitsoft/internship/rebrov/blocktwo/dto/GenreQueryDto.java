package dev.profitsoft.internship.rebrov.blocktwo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreQueryDto {
    @Size(min = 2, max = 70, message = "Genre name must be between 2 and 70 characters")
    @NotNull
    private String name;
}
