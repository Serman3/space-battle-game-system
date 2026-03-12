package ru.otus.game_service.broker;

import org.apache.kafka.clients.admin.NewTopic;
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
    private final String consumerGroupId;
    private final String usersEventsTopicName;
    private final String gamesEventsTopicName;
    private final String gameCommandsTopicName;
   // private final String autoOffsetReset;

    @Autowired
    public KafkaConfig(Environment environment) {
        super(Arrays.stream(environment.getProperty("spring.kafka.bootstrap-servers").split(",")).toList());
        this.consumerGroupId = environment.getProperty("spring.kafka.consumer.group-id");
        this.usersEventsTopicName = environment.getProperty("kafka.topics.users.events.topic.name");
        this.gamesEventsTopicName = environment.getProperty("kafka.topics.games.events.topic.name");
        this.gameCommandsTopicName = environment.getProperty("kafka.topics.games.commands.topic.name");
        //   this.autoOffsetReset = environment.getProperty("spring.kafka.consumer.auto-offset-reset");
    }

    @Bean
    public ConsumerFactory<String, Object> consumerUserEventFactory() {
        Map<String, Object> consumerConfigProps = consumerConfig(consumerGroupId);
        //consumerConfigProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        return new DefaultKafkaConsumerFactory<>(consumerConfigProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerUserEventFactory());
        return factory;
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

    @Bean
    public NewTopic gamesEventsTopic() {
        return TopicBuilder.name(gamesEventsTopicName)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }

    @Bean
    public NewTopic gameCommandsTopic() {
        return TopicBuilder.name(gameCommandsTopicName)
                .partitions(TOPIC_PARTITIONS)
                .replicas(TOPIC_REPLICATION_FACTOR)
                .build();
    }
}
