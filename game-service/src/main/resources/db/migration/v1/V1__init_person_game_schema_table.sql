CREATE TABLE IF NOT EXISTS person_game.t_user
(
    id       SERIAL                      PRIMARY KEY,
    active   BOOLEAN                     NOT NULL DEFAULT TRUE,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    username VARCHAR                     NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS person_game.t_game
(
    id           SERIAL                      PRIMARY KEY,
    active       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game      VARCHAR                     NOT NULL UNIQUE,
    status       VARCHAR                     NOT NULL
);

CREATE TABLE IF NOT EXISTS person_game.t_game_event
(
    id           SERIAL                      PRIMARY KEY,
    active       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game      UUID                        NOT NULL,
    status       VARCHAR                     NOT NULL,
    UNIQUE (id_game, status)
);

CREATE TABLE IF NOT EXISTS person_game.t_active_game
(
    id           SERIAL                      PRIMARY KEY,
    active       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game      INTEGER                     NOT NULL REFERENCES person_game.t_game (id),
    id_user      INTEGER                     NOT NULL REFERENCES person_game.t_user (id),
    UNIQUE (id_user, id_game)
);