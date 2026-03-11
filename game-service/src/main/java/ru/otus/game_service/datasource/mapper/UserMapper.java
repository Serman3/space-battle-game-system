package ru.otus.game_service.datasource.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.otus.game_service.datasource.dto.UserDto;
import ru.otus.game_service.datasource.entity.UserEntity;
import ru.otus.shared.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class UserMapper implements Mapper<UserDto, UserEntity> {

    private final ModelMapper modelMapper;

    @Override
    public UserEntity mapToEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @Override
    public UserDto mapToDto(UserEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}
