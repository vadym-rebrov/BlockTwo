package dev.profitsoft.internship.rebrov.blocktwo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.websocket.OnOpen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorQueryDto {
    @Pattern(
            regexp = "^(?! )[A-Za-zА-Яа-яЇїІіЄєЁё]+( [A-Za-zА-Яа-яЇїІіЄєЁё]+)*(?<! )$",
            message = "Director name must contain only letters and spaces, without leading/trailing spaces"
    )
    @Size(min = 2, max = 70, message = "Director name must be between 2 and 70 characters")
    @NotNull
    private String name;
}
