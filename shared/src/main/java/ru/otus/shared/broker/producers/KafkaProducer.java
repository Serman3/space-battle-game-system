package ru.otus.shared.broker.producers;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

public interface KafkaProducer<K, V> {

    CompletableFuture<SendResult<K, V>> sendMessage(ProducerRecord<K, V> producerRecord);
}
