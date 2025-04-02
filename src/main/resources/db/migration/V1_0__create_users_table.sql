CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       keycloak_id VARCHAR(255) UNIQUE NOT NULL,
                       username VARCHAR(255)
);