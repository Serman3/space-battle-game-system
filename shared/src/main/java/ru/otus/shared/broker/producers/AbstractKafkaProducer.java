package ru.otus.shared.broker.producers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AbstractKafkaProducer<K,V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    protected AbstractKafkaProducer(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    protected CompletableFuture<SendResult<K, V>> send(ProducerRecord<K,V> producerRecord) {
        try {
            return kafkaTemplate.send(producerRecord);
        } catch (Exception e) {
            log.error("""
                    Не удалось отправить сообщение в топик: {}
                    С ключом: {}
                    Значение: {}
                    """, producerRecord.topic(), producerRecord.key(), producerRecord.value());
            throw e;
        }
    }

}
