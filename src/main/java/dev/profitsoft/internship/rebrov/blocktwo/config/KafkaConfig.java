package dev.profitsoft.internship.rebrov.blocktwo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String MOVIE_CREATED_TOPIC = "movie-created-events";

    @Bean
    public NewTopic movieCreatedTopic() {
        return TopicBuilder.name(MOVIE_CREATED_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}