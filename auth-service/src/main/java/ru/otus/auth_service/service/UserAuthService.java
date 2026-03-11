package ru.otus.auth_service.service;

import jakarta.transaction.Transactional;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.otus.auth_service.datasource.dto.DeactivateTokenDto;
import ru.otus.auth_service.datasource.dto.UserAuthorityDto;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.datasource.entity.DeactivatedTokenEntity;
import ru.otus.auth_service.datasource.entity.UserAuthorityEntity;
import ru.otus.auth_service.datasource.entity.UserEntity;
import ru.otus.shared.mapper.Mapper;
import ru.otus.auth_service.datasource.repository.DeactivatedTokenRepository;
import ru.otus.auth_service.datasource.repository.UserAuthorityRepository;
import ru.otus.auth_service.datasource.repository.UserRepository;
import ru.otus.shared.broker.event.UserRegisteredEvent;
import ru.otus.shared.broker.producers.KafkaProducer;
import ru.otus.shared.security.token.Token;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserAuthService {

    private final Mapper<UserDto, UserEntity> userMapper;

    private final Mapper<UserAuthorityDto, UserAuthorityEntity> userAuthorityMapper;

    private final Mapper<DeactivateTokenDto, DeactivatedTokenEntity> deactivatedTokenMapper;

    private final UserRepository userRepository;

    private final UserAuthorityRepository userAuthorityRepository;

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    private final KafkaProducer<String, Object> userCreatedEventProducer;

    private final String usersRegisteredEventsTopicName;

    @Autowired
    public UserAuthService(Environment environment,
                           Mapper<UserDto, UserEntity> userMapper,
                           Mapper<UserAuthorityDto, UserAuthorityEntity> userAuthorityMapper,
                           Mapper<DeactivateTokenDto, DeactivatedTokenEntity> deactivatedTokenMapper,
                           UserRepository userRepository,
                           UserAuthorityRepository userAuthorityRepository,
                           DeactivatedTokenRepository deactivatedTokenRepository,
                           KafkaProducer<String, Object> userCreatedEventProducer) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.deactivatedTokenMapper = deactivatedTokenMapper;
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.deactivatedTokenRepository = deactivatedTokenRepository;
        this.userCreatedEventProducer = userCreatedEventProducer;
        this.usersRegisteredEventsTopicName = environment.getProperty("kafka.topics.users.events.topic.name");
    }

    public boolean existsDeactivateToken(Token token) {
        return deactivatedTokenRepository.existsDeactivateTokenById(token.id());
    }

    public Optional<UserDto> findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .stream()
                .map(userMapper::mapToDto)
                .findFirst();
    }

    @Transactional
    public void addUser(UserDto userDto) {
        UserEntity userEntity = userMapper.mapToEntity(userDto);
        userEntity.setPassword("{noop}" + userEntity.getPassword());
        userEntity.setActive(true);
        userEntity.setCreated(Instant.now());
        userEntity.setUpdated(Instant.now());
        userRepository.save(userEntity);

        addRoleByUsername(new UserAuthorityDto(userEntity.getUsername(), "ROLE_USER"));

        userCreatedEventProducer.sendMessage(new ProducerRecord<>(usersRegisteredEventsTopicName, new UserRegisteredEvent(userEntity.getUsername())));
    }

    public void addRoleByUsername(UserAuthorityDto userAuthorityDto) {
        UserAuthorityEntity userAuthorityEntity = userAuthorityMapper.mapToEntity(userAuthorityDto);
        userAuthorityEntity.setActive(true);
        userAuthorityEntity.setCreated(Instant.now());
        userAuthorityEntity.setUpdated(Instant.now());
        userAuthorityRepository.save(userAuthorityEntity);
    }

    @Transactional
    public void addDeactivateToken(DeactivateTokenDto deactivateTokenDto) {
        DeactivatedTokenEntity deactivatedTokenEntity = deactivatedTokenMapper.mapToEntity(deactivateTokenDto);
        deactivatedTokenEntity.setActive(true);
        deactivatedTokenEntity.setCreated(Instant.now());
        deactivatedTokenEntity.setUpdated(Instant.now());
        deactivatedTokenRepository.save(deactivatedTokenEntity);
    }

    public List<String> findAllUserRolesByUserId(Long id) {
        return userAuthorityRepository.findAllUserRolesByUserId(id);
    }

    public List<String> findAllUserRolesByUserName(String username) {
        return userAuthorityRepository.findAllUserRolesByUserName(username);
    }

}
