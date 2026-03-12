package ru.otus.auth_service.broker;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import ru.otus.shared.broker.config.DefaultKafkaConfig;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class KafkaConfig extends DefaultKafkaConfig {

    private static final Integer TOPIC_REPLICATION_FACTOR = 3;
    private static final Integer TOPIC_PARTITIONS = 3;
    private final String usersEventsTopicName;

    @Autowired
    public KafkaConfig(Environment environment) {
        super(Arrays.stream(environment.getProperty("spring.kafka.bootstrap-servers").split(",")).toList());
        this.usersEventsTopicName = environment.getProperty("kafka.topics.users.events.topic.name");
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> producerConfigProps = producerConfig();
        return new DefaultKafkaProducerFactory<>(producerConfigProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic usersEventsTopic() {
        return TopicBuilder.name(usersEventsTopicName)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
