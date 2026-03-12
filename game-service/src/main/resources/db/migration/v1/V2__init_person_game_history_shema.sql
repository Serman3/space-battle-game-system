CREATE TABLE IF NOT EXISTS person_game_history.revinfo
(
    rev       BIGSERIAL PRIMARY KEY,
    revtmstmp BIGINT
);

CREATE TABLE IF NOT EXISTS person_game_history.t_user_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    username      VARCHAR                     NOT NULL,

    CONSTRAINT pk_t_user_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_user_history_rev FOREIGN KEY (revision) REFERENCES person_game_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_user_history_revision ON person_game_history.t_user_history (revision);

CREATE TABLE IF NOT EXISTS person_game_history.t_game_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game       VARCHAR                     NOT NULL,

    CONSTRAINT pk_t_game_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_game_history_rev FOREIGN KEY (revision) REFERENCES person_game_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_game_history_revision ON person_game_history.t_game_history (revision);

CREATE TABLE IF NOT EXISTS person_game_history.t_game_event_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    game_id       UUID                        NOT NULL,
    status        VARCHAR                     NOT NULL,

    CONSTRAINT pk_t_game_event_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_game_event_history_rev FOREIGN KEY (revision) REFERENCES person_game_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_game_event_history_revision ON person_game_history.t_game_event_history (revision);

CREATE TABLE IF NOT EXISTS person_game_history.t_active_game_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game       INTEGER                     NOT NULL,
    id_user       INTEGER                     NOT NULL,

    CONSTRAINT pk_t_active_game_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_active_game_history_rev FOREIGN KEY (revision) REFERENCES person_game_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_active_game_history_revision ON person_game_history.t_active_game_history (revision);