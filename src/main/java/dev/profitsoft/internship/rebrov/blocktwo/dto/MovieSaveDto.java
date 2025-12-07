package dev.profitsoft.internship.rebrov.blocktwo.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovieSaveDto{
    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title must be <= 255 characters")
    private String title;

    @NotNull(message = "Release date is required")
    @PastOrPresent(message = "Release date cannot be in the future")
    private LocalDate released;

    @NotEmpty(message = "Genres list cannot be empty")
    private Set<@NotNull(message = "Genre id cannot be null") @Positive Long> genresId;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be >= 0.0")
    @DecimalMax(value = "10.0", message = "Rating must be <= 10.0")
    private Double rating;

    @NotNull(message = "Director id is required")
    @Positive
    private Long directorId;

    @NotNull(message = "Awards list must not be null")
    private List<@NotBlank(message = "Award name cannot be blank") String> awards;
}
