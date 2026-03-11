package ru.otus.game_service.broker;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
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
    private final String consumerGroupId;
    private final String autoOffsetReset;

    @Autowired
    public KafkaConfig(Environment environment) {
        super(Arrays.stream(environment.getProperty("spring.kafka.bootstrap-servers").split(",")).toList());
        this.usersEventsTopicName = environment.getProperty("kafka.topics.users.events.topic.name");
        this.consumerGroupId = environment.getProperty("spring.kafka.consumer.group-id");
        this.autoOffsetReset = environment.getProperty("spring.kafka.consumer.auto-offset-reset");
    }

    @Bean
    public ConsumerFactory<String, String> consumerUserCreatedEventFactory() {
        Map<String, Object> consumerConfigProps = consumerConfig(consumerGroupId);
        consumerConfigProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return new DefaultKafkaConsumerFactory<>(consumerConfigProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerUserCreatedEventFactory());
        return factory;
    }

    @Bean
    public NewTopic usersRegisteredEventsTopic() {
        return TopicBuilder.name(usersEventsTopicName)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
