package ru.otus.game_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.game_service.datasource.dto.UserDto;
import ru.otus.game_service.datasource.entity.UserEntity;
import ru.otus.game_service.datasource.mapper.UserMapper;
import ru.otus.game_service.datasource.repository.UserRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void createUser(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(userDto.getUsername());

        if (userEntityOptional.isEmpty()) {
            UserEntity userEntity = userMapper.mapToEntity(userDto);
            userEntity.setActive(true);
            userEntity.setUpdated(Instant.now());
            userEntity.setCreated(Instant.now());

            userRepository.save(userEntity);
        }
    }

    @Override
    @Transactional
    public void cancelledUser(UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(userDto.getUsername());
        userEntityOptional.ifPresent(userEntity -> {
            userRepository.deleteById(userEntity.getId());
        });
    }
}
