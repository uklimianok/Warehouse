CREATE SEQUENCE position_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE positions (
    id BIGINT PRIMARY KEY DEFAULT nextval('position_seq'),
    name VARCHAR(255) UNIQUE NOT NULL,
    code_name VARCHAR(255) UNIQUE NOT NULL,
    has_database_access BOOLEAN NOT NULL
);

ALTER SEQUENCE position_seq OWNED BY positions.id;