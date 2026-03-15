package ru.otus.shared.broker.user.events;

import ru.otus.shared.utils.UserStatus;

public class UserEventFactory {

    private final String username;

    public UserEventFactory(String username) {
        this.username = username;
    }

    public UserEvent getUserEvent(UserStatus userStatus) {
        return switch (userStatus) {
            case CREATED -> new UserCreatedEvent(username);
            case CANCELLED -> new UserCancelledEvent(username);
        };
    }
}
