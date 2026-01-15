package dev.profitsoft.internship.rebrov.blocktwo.dto;

import dev.profitsoft.internship.rebrov.blocktwo.data.Movie;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
public class MessageEventDto {
    private String requestId;
    private List<String> recipientEmails;
    private String subject;
    private String content;

    public MessageEventDto(Movie movie, List<String> recipientEmails){
        this.requestId = UUID.randomUUID().toString();
        this.recipientEmails = recipientEmails;
        this.subject = movie.getTitle();
        this.content = String.format("Created new movie with id = %s: %s (%s)", movie.getId(), movie.getTitle(), movie.getReleased().getYear());
    }
}
