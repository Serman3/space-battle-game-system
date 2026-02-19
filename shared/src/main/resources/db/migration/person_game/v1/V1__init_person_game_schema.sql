CREATE TABLE person_game.t_game
(
    id           SERIAL                      PRIMARY KEY,
    active       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game      VARCHAR                     NOT NULL UNIQUE,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);

CREATE TABLE person_game.t_active_game
(
    id           SERIAL                      PRIMARY KEY,
    active       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game      INTEGER                     NOT NULL REFERENCES person_game.t_game (id),
    id_user      INTEGER                     NOT NULL REFERENCES person.t_user (id),
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);