package dev.profitsoft.internship.rebrov.blocktwo.dto;
import dev.profitsoft.internship.rebrov.blocktwo.validation.ValidIntervals;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ValidIntervals
@ToString
public class MovieQueryDto{

    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    private String country;

    @Pattern(
            regexp = "^(?! )[A-Za-zА-Яа-яЇїІіЄєЁё]+( [A-Za-zА-Яа-яЇїІіЄєЁё]+)*(?<! )$",
            message = "Director name must contain only letters and spaces, without leading/trailing spaces"
    )
    @Size(min = 2, max = 70, message = "Director name must be between 2 and 70 characters")
    private String directorName;

    @DecimalMin(value = "0.0", message = "Rating must be >= 0")
    @DecimalMax(value = "10.0", message = "Rating must be <= 10")
    private Double minRating;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    private Double maxRating;

    @Min(value = 1900, message = "Year must be >= 1900")
    @Max(value = 2100, message = "Year must be <= 2100")
    private Integer minYear;

    @Min(1900)
    @Max(2100)
    private Integer maxYear;

    @Min(value = 0, message = "Page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be >= 1")
    @Max(value = 100, message = "Size must be <= 100")
    private Integer size = 20;
}
