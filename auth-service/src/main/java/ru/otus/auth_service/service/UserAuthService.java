package ru.otus.auth_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.auth_service.datasource.dto.DeactivateTokenDto;
import ru.otus.auth_service.datasource.dto.UserAuthorityDto;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.datasource.entity.DeactivatedTokenEntity;
import ru.otus.auth_service.datasource.entity.UserAuthorityEntity;
import ru.otus.auth_service.datasource.entity.UserEntity;
import ru.otus.auth_service.datasource.entity.UserOutboxEventEntity;
import ru.otus.auth_service.datasource.repository.UserOutboxRepository;
import ru.otus.auth_service.ex.UserProcessingException;
import ru.otus.shared.mapper.Mapper;
import ru.otus.auth_service.datasource.repository.DeactivatedTokenRepository;
import ru.otus.auth_service.datasource.repository.UserAuthorityRepository;
import ru.otus.auth_service.datasource.repository.UserRepository;
import ru.otus.shared.security.token.Token;
import ru.otus.shared.utils.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserAuthService {

    private final Mapper<UserDto, UserEntity> userMapper;

    private final Mapper<UserAuthorityDto, UserAuthorityEntity> userAuthorityMapper;

    private final Mapper<DeactivateTokenDto, DeactivatedTokenEntity> deactivatedTokenMapper;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private final UserAuthorityRepository userAuthorityRepository;

    private final DeactivatedTokenRepository deactivatedTokenRepository;

    private final UserOutboxRepository userOutboxRepository;

    @Autowired
    public UserAuthService(Mapper<UserDto, UserEntity> userMapper,
                           Mapper<UserAuthorityDto, UserAuthorityEntity> userAuthorityMapper,
                           Mapper<DeactivateTokenDto, DeactivatedTokenEntity> deactivatedTokenMapper,
                           ObjectMapper objectMapper,
                           UserRepository userRepository,
                           UserAuthorityRepository userAuthorityRepository,
                           DeactivatedTokenRepository deactivatedTokenRepository,
                           UserOutboxRepository userOutboxRepository) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.deactivatedTokenMapper = deactivatedTokenMapper;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.deactivatedTokenRepository = deactivatedTokenRepository;
        this.userOutboxRepository = userOutboxRepository;
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
    public void createUser(UserDto userDto) {
        UserEntity userEntity = saveUser(userDto);
        addRoleByUsername(new UserAuthorityDto(userEntity.getUsername(), "ROLE_USER"));
        saveUserEvent(userEntity.getUsername(), UserStatus.CREATED);
    }

    @Transactional
    public boolean cancelUser(UserDto userDto) {
        UserEntity userEntity = updateUser(userDto, UserStatus.CANCELLED);
        saveUserEvent(userEntity.getUsername(), UserStatus.CANCELLED);
        return true;
    }

    private UserEntity saveUser(UserDto userDto) {
        try {
            UserEntity userEntity = userMapper.mapToEntity(userDto);
            userEntity.setPassword("{noop}" + userEntity.getPassword());
            userEntity.setActive(true);
            userEntity.setCreated(Instant.now());
            userEntity.setUpdated(Instant.now());
            userEntity.setStatus(UserStatus.CREATED);
            userRepository.save(userEntity);
            return userEntity;
        } catch (Exception e) {
            throw new UserProcessingException(e.getMessage());
        }
    }

    private UserEntity updateUser(UserDto userDto, UserStatus userStatus) {
        try {
            UserEntity userEntity = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new UserProcessingException("Username " + userDto.getUsername() + " not found"));
            switch (userStatus) {
                case CANCELLED -> {
                    userEntity.setActive(false);
                    userEntity.setStatus(UserStatus.CANCELLED);
                    userEntity.setUpdated(Instant.now());
                }
                case CREATED -> {
                    userEntity.setActive(true);
                    userEntity.setStatus(UserStatus.CREATED);
                    userEntity.setUpdated(Instant.now());
                }
            }
            userRepository.save(userEntity);
            return userEntity;
        } catch (Exception e) {
            throw new UserProcessingException(e.getMessage());
        }
    }

    private void addRoleByUsername(UserAuthorityDto userAuthorityDto) {
        try {
            UserAuthorityEntity userAuthorityEntity = userAuthorityMapper.mapToEntity(userAuthorityDto);
            userAuthorityEntity.setActive(true);
            userAuthorityEntity.setCreated(Instant.now());
            userAuthorityEntity.setUpdated(Instant.now());
            userAuthorityRepository.save(userAuthorityEntity);
        } catch (Exception e) {
            throw new UserProcessingException(e.getMessage());
        }
    }

    private void saveUserEvent(String eventPayload, UserStatus eventType) {
        try {
            UserOutboxEventEntity userOutboxEventEntity = new UserOutboxEventEntity();
            userOutboxEventEntity.setActive(true);
            userOutboxEventEntity.setCreated(Instant.now());
            userOutboxEventEntity.setUpdated(Instant.now());
            userOutboxEventEntity.setEventType(eventType);
            userOutboxEventEntity.setEventPayload(eventPayload);
            userOutboxRepository.save(userOutboxEventEntity);
        } catch (Exception e) {
            throw new UserProcessingException(e.getMessage());
        }
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
