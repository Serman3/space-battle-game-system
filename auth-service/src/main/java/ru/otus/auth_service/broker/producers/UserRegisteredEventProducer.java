package ru.otus.auth_service.broker.producers;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.otus.shared.broker.event.UserRegisteredEvent;
import ru.otus.shared.broker.producers.AbstractKafkaProducer;
import ru.otus.shared.broker.producers.KafkaProducer;

import java.util.concurrent.CompletableFuture;

@Component
public class UserRegisteredEventProducer extends AbstractKafkaProducer<String, UserRegisteredEvent> implements KafkaProducer<String, UserRegisteredEvent> {

    @Autowired
    protected UserRegisteredEventProducer(KafkaTemplate<String, UserRegisteredEvent> userRegisteredkafkaTemplate) {
        super(userRegisteredkafkaTemplate);
    }

    @Override
    public CompletableFuture<SendResult<String, UserRegisteredEvent>> sendMessage(ProducerRecord<String, UserRegisteredEvent> producerRecord) {
        return send(producerRecord);
    }
}
