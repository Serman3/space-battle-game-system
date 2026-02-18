CREATE TABLE person.t_user
(
    id       SERIAL                      PRIMARY KEY,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    username VARCHAR                     NOT NULL UNIQUE,
    password VARCHAR                     NOT NULL
);

CREATE TABLE person.t_user_authority
(
    id          SERIAL                      PRIMARY KEY,
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_user     INTEGER                     NOT NULL REFERENCES person.t_user (id),
    c_authority VARCHAR                     NOT NULL,
    UNIQUE (id_user, c_authority)
);

CREATE TABLE person.t_deactivated_token
(
    id           UUID                        PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    c_keep_until TIMESTAMP WITHOUT TIME ZONE NOT NULL CHECK ( c_keep_until > now() )
);