package dev.profitsoft.internship.rebrov.blocktwo.service;

import dev.profitsoft.internship.rebrov.blocktwo.config.KafkaConfig;
import dev.profitsoft.internship.rebrov.blocktwo.dto.MessageEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMovieCreatedEvent(MessageEventDto event) {
        log.info("Sending movie creation event for movie: {}", event.getSubject());
        try {
            kafkaTemplate.send(KafkaConfig.MOVIE_CREATED_TOPIC, event.getRequestId(), event);
        } catch (Exception e) {
            log.error("Error sending message to Kafka", e);
        }
    }
}