package ru.otus.game_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.game_service.datasource.dto.UserDto;
import ru.otus.game_service.datasource.entity.UserEntity;
import ru.otus.game_service.datasource.mapper.UserMapper;
import ru.otus.game_service.datasource.repository.UserRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        UserEntity userEntity = userMapper.mapToEntity(userDto);
        userEntity.setActive(true);
        userEntity.setUpdated(Instant.now());
        userEntity.setCreated(Instant.now());

        userRepository.save(userEntity);
    }
}
