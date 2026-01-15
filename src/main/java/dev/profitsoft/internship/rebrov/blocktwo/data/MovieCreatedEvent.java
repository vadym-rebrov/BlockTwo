package dev.profitsoft.internship.rebrov.blocktwo.data;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
public class MovieCreatedEvent {
    private Long id;
    private String title;
    private LocalDate year;
}
