package ru.otus.shared.broker.event.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserCreatedEvent implements UserEvent {

    private String username;
}
