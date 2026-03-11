package ru.otus.game_service.broker.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.otus.game_service.datasource.dto.UserDto;
import ru.otus.shared.broker.event.UserRegisteredEvent;
import ru.otus.shared.mapper.Mapper;

@Component
@RequiredArgsConstructor
public class UserEventMapper implements Mapper<UserDto, UserRegisteredEvent> {

    private ModelMapper modelMapper;

    @Override
    public UserRegisteredEvent mapToEntity(UserDto dto) {
        return modelMapper.map(dto, UserRegisteredEvent.class);
    }

    @Override
    public UserDto mapToDto(UserRegisteredEvent event) {
        return modelMapper.map(event, UserDto.class);
    }
}
