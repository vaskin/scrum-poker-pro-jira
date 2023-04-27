CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE token
(
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    access_token  VARCHAR     NOT NULL,
    refresh_token VARCHAR     NOT NULL,
    cloud_id      VARCHAR,
    user_id       UUID UNIQUE NOT NULL,
    created       TIMESTAMP   NOT NULL,
    modified      TIMESTAMP   NOT NULL
);