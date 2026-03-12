package ru.otus.auth_service.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.otus.auth_service.datasource.entity.UserOutboxEventEntity;
import ru.otus.auth_service.datasource.repository.UserOutboxRepository;
import ru.otus.shared.broker.user.events.UserEvent;
import ru.otus.shared.broker.user.events.UserEventFactory;

import java.util.List;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class UserOutboxServiceImpl implements UserOutboxService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final String usersRegisteredEventsTopicName;

    private final UserOutboxRepository userOutboxRepository;

    @Autowired
    public UserOutboxServiceImpl(KafkaTemplate<String, Object> kafkaTemplate,
                                 Environment environment,
                                 UserOutboxRepository userOutboxRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.usersRegisteredEventsTopicName = environment.getProperty("kafka.topics.users.events.topic.name");
        this.userOutboxRepository = userOutboxRepository;
    }

    @Override
    @Scheduled(fixedRateString = "${kafka.scheduled}")
    public void eventProcessing() {
        List<UserOutboxEventEntity> listOfOutboxEvents = userOutboxRepository.findAll();
        log.info("Number of outbox events: {}", listOfOutboxEvents.size());

        if (!listOfOutboxEvents.isEmpty()) {
            for (UserOutboxEventEntity userOutboxEvent : listOfOutboxEvents) {
                log.info("Sending event to Kafka topic {}", usersRegisteredEventsTopicName);
                sendEventToKafka(userOutboxEvent.getEventType().name(), new UserEventFactory(userOutboxEvent.getEventPayload()).getUserEvent(userOutboxEvent.getEventType()));
                userOutboxRepository.deleteById(userOutboxEvent.getId());
            }
        }
    }

    private void sendEventToKafka(String eventType, UserEvent userEvent) {
        try {
            CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(new ProducerRecord<>(usersRegisteredEventsTopicName, eventType, userEvent));
            SendResult<String, Object> sendResult = completableFuture.get();
            log.info("Partition: {}", sendResult.getRecordMetadata().partition());
        } catch (Throwable e) {
            log.error("Error sending event to kafka: {}", e.getMessage());
        }
    }
}
