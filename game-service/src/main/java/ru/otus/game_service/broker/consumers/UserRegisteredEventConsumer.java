package ru.otus.game_service.broker.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.game_service.broker.mapper.UserEventMapper;
import ru.otus.game_service.service.UserService;
import ru.otus.shared.broker.consumers.KafkaConsumer;
import ru.otus.shared.broker.event.UserRegisteredEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventConsumer implements KafkaConsumer<String, UserRegisteredEvent> {

    private final UserService userService;
    private final UserEventMapper userEventMapper;

    @Override
    @KafkaListener(topics = "${kafka.topics.users.events.topic.name}")
    public void processMessage(ConsumerRecord<String, UserRegisteredEvent> consumerRecord) {
        UserRegisteredEvent message = consumerRecord.value();
        log.info("Received message for user: {}", message);
        try {
            userService.saveUser(userEventMapper.mapToDto(message));
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
