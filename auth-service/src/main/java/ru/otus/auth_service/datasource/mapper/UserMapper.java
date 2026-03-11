package ru.otus.auth_service.datasource.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.datasource.entity.UserEntity;
import ru.otus.shared.mapper.Mapper;

@Component
public class UserMapper implements Mapper<UserDto, UserEntity> {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserEntity mapToEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @Override
    public UserDto mapToDto(UserEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}
