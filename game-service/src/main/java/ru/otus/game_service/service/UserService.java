package ru.otus.game_service.service;

import ru.otus.game_service.datasource.dto.UserDto;

public interface UserService {

    void saveUser(UserDto userDto);
}
