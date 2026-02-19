package ru.otus.auth_service.datasource.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.auth_service.datasource.dto.UserAuthorityDto;
import ru.otus.auth_service.datasource.entity.UserAuthorityEntity;
import ru.otus.auth_service.datasource.repository.UserRepository;

@Component
public class UserAuthorityMapper implements Mapper<UserAuthorityDto, UserAuthorityEntity> {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthorityMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAuthorityEntity mapToEntity(UserAuthorityDto dto) {
        return userRepository.findByUsername(dto.getUsername())
                .stream()
                .map(userEntity -> {
                    UserAuthorityEntity userAuthorityEntity = new UserAuthorityEntity();
                    userAuthorityEntity.setUser(userEntity);
                    userAuthorityEntity.setAuthority(dto.getRole());
                    return userAuthorityEntity;
                })
                .findFirst()
                .orElseThrow();
    }

    @Override
    public UserAuthorityDto mapToDto(UserAuthorityEntity entity) {
        UserAuthorityDto userAuthorityDto = new UserAuthorityDto();
        userAuthorityDto.setUsername(entity.getUser().getUsername());
        userAuthorityDto.setRole(entity.getAuthority());
        return userAuthorityDto;
    }
}
