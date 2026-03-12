package ru.otus.shared.broker.user.events;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreatedEvent implements UserEvent {

    private String username;
}
