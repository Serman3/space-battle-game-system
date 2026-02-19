package ru.otus.auth_service.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.auth_service.datasource.dto.DeactivateTokenDto;
import ru.otus.auth_service.datasource.dto.UserAuthorityDto;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.datasource.entity.DeactivatedTokenEntity;
import ru.otus.auth_service.datasource.entity.UserAuthorityEntity;
import ru.otus.auth_service.datasource.entity.UserEntity;
import ru.otus.auth_service.datasource.mapper.Mapper;
import ru.otus.auth_service.datasource.repository.DeactivatedTokenRepository;
import ru.otus.auth_service.datasource.repository.UserAuthorityRepository;
import ru.otus.auth_service.datasource.repository.UserRepository;
import ru.otus.security.token.Token;

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

    @Autowired
    public UserAuthService(Mapper<UserDto, UserEntity> userMapper,
                           Mapper<UserAuthorityDto, UserAuthorityEntity> userAuthorityMapper,
                           Mapper<DeactivateTokenDto, DeactivatedTokenEntity> deactivatedTokenMapper,
                           UserRepository userRepository,
                           UserAuthorityRepository userAuthorityRepository,
                           DeactivatedTokenRepository deactivatedTokenRepository) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.deactivatedTokenMapper = deactivatedTokenMapper;
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.deactivatedTokenRepository = deactivatedTokenRepository;
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
        userRepository.save(userEntity);

        addRoleByUsername(new UserAuthorityDto(userEntity.getUsername(), "ROLE_USER"));
    }

    public void addRoleByUsername(UserAuthorityDto userAuthorityDto) {
        UserAuthorityEntity userAuthorityEntity = userAuthorityMapper.mapToEntity(userAuthorityDto);
        userAuthorityRepository.save(userAuthorityEntity);
    }

    @Transactional
    public void addDeactivateToken(DeactivateTokenDto deactivateTokenDto) {
        DeactivatedTokenEntity deactivatedTokenEntity = deactivatedTokenMapper.mapToEntity(deactivateTokenDto);
        deactivatedTokenRepository.save(deactivatedTokenEntity);
    }

    public List<String> findAllUserRolesByUserId(Long id) {
        return userAuthorityRepository.findAllUserRolesByUserId(id);
    }

    public List<String> findAllUserRolesByUserName(String username) {
        return userAuthorityRepository.findAllUserRolesByUserName(username);
    }

}
