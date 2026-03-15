package ru.otus.shared.broker.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface KafkaConsumer<K,V> {

    void processMessage(ConsumerRecord<K, V> consumerRecord);
}
