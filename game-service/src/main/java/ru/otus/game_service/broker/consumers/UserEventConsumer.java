package ru.otus.game_service.broker.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.game_service.datasource.dto.UserDto;
import ru.otus.game_service.service.UserService;
import ru.otus.shared.broker.event.user.UserCancelledEvent;
import ru.otus.shared.broker.event.user.UserCreatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(id = "${spring.kafka.consumer.group-id}", topics = "${kafka.topics.users.events.topic.name}")
public class UserEventConsumer {

    private final UserService userService;

    @KafkaHandler
    public void processCreated(UserCreatedEvent userCreatedEvent) {
        log.info("Received message for user created event: {}", userCreatedEvent);
        UserDto userDto = new UserDto(userCreatedEvent.getUsername());
        try {
            userService.createUser(userDto);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    @KafkaHandler
    public void processCancelled(UserCancelledEvent userCancelledEvent) {
        log.info("Received message for user cancelled event: {}", userCancelledEvent);
        UserDto userDto = new UserDto(userCancelledEvent.getUsername());
        try {
            userService.cancelledUser(userDto);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}
