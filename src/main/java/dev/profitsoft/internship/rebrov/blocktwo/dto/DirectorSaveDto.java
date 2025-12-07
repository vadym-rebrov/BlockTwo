package dev.profitsoft.internship.rebrov.blocktwo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DirectorSaveDto {

    @NotBlank(message = "Full name cannot be empty")
    @Pattern(
            regexp = "^[A-Za-z]+(?:\\s+[A-Za-z]+)*$",
            message = "Full name must contain 1 or 2 words using letters only"
    )
    private String fullName;

    @NotNull(message = "Director id is required")
    @Positive
    private Long countryId;

    @NotNull(message = "Birthday cannot be null")
    @PastOrPresent(message = "Birthday must be in the past or today")
    private LocalDate birthday;
}