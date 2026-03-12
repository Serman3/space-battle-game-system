package ru.otus.auth_service.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.shared.broker.game.events.GameOrganizedEvent;

import java.util.List;
import java.util.UUID;

@Service
public class SpaceBattleServiceImpl implements SpaceBattleService {

    private final String gameEventTopicName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public SpaceBattleServiceImpl(Environment environment,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
        this.gameEventTopicName = environment.getProperty("kafka.topics.games.events.topic.name");
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String organizeSpaceBattle(List<String> users) {
        UUID gameId = UUID.randomUUID();
        GameOrganizedEvent gameOrganizedEvent = new GameOrganizedEvent(gameId, users);

        kafkaTemplate.send(new ProducerRecord<>(gameEventTopicName, gameOrganizedEvent));
        return gameId.toString();
    }
}
