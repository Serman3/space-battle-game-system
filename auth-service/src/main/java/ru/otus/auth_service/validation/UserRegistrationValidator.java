package ru.otus.auth_service.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.ex.UserNotCreatedException;
import ru.otus.auth_service.openapi.model.RegistrationRequestDto;
import ru.otus.auth_service.service.UserAuthService;
import ru.otus.shared.validator.BaseValidator;

import java.util.Optional;

@Component
public class UserRegistrationValidator extends BaseValidator {

    private final UserAuthService userAuthService;

    @Autowired
    public UserRegistrationValidator(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RegistrationRequestDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationRequestDto registrationRequest = (RegistrationRequestDto) target;

        checkErrors(errors, UserNotCreatedException::new);

        Optional<UserDto> userDtoOptional = userAuthService.findUserByUsername(registrationRequest.getUsername());
        if (userDtoOptional.isPresent()) {
            throw new UserNotCreatedException("Такой пользователь уже существует");
        }
    }

}
