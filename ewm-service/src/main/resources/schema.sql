CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        BIGINT REFERENCES categories (id),
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP     NOT NULL,
    location_lat       REAL          NOT NULL,
    location_lon       REAL          NOT NULL,
    paid               BOOLEAN       NOT NULL,
    participant_limit  INTEGER       NOT NULL,
    request_moderation BOOLEAN       NOT NULL,
    title              VARCHAR(120)  NOT NULL,
    confirmed_requests INTEGER DEFAULT 0,
    created_on         TIMESTAMP     NOT NULL,
    initiator_id       BIGINT        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    published_on       TIMESTAMP,
    state              VARCHAR(30)   NOT NULL,
    views              BIGINT  DEFAULT 0
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    event_id     BIGINT      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    created      TIMESTAMP   NOT NULL,
    status       VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN     NOT NULL,
    title  VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL REFERENCES compilations (id) ON DELETE CASCADE,
    event_id       BIGINT NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);

