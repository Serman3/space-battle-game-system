package ru.otus.shared.broker.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisteredEvent {

    private String username;

}
