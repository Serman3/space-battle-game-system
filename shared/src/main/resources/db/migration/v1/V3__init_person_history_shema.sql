CREATE TABLE IF NOT EXISTS person_history.revinfo
(
    rev       BIGSERIAL PRIMARY KEY,
    revtmstmp BIGINT
);

CREATE TABLE IF NOT EXISTS person_history.t_user_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    username      VARCHAR                     NOT NULL,
    password      VARCHAR                     NOT NULL,

    CONSTRAINT pk_t_user_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_user_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_user_history_revision ON person_history.t_user_history (revision);

CREATE TABLE IF NOT EXISTS person_history.t_user_authority_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_user       INTEGER                     NOT NULL,
    c_authority   VARCHAR                     NOT NULL,

    CONSTRAINT pk_t_user_authority_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_user_authority_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_user_authority_history_revision ON person_history.t_user_authority_history (revision);

CREATE TABLE IF NOT EXISTS person_history.t_deactivated_token_history
(
    id            UUID                        NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    c_keep_until  TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_t_deactivated_token_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_deactivated_token_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_deactivated_token_history_revision ON person_history.t_deactivated_token_history (revision);

CREATE TABLE IF NOT EXISTS person_history.t_game_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game       VARCHAR                     NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_t_game_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_game_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_game_history_revision ON person_history.t_game_history (revision);

CREATE TABLE IF NOT EXISTS person_history.t_active_game_history
(
    id            SERIAL                      NOT NULL,
    revision      BIGINT                      NOT NULL,
    revision_type SMALLINT                    NOT NULL,
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    id_game       INTEGER                     NOT NULL,
    id_user       INTEGER                     NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT pk_t_active_game_history     PRIMARY KEY (id, revision),
    CONSTRAINT fk_t_active_game_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_t_active_game_history_revision ON person_history.t_active_game_history (revision);