CREATE SEQUENCE status_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE statuses (
    id BIGINT PRIMARY KEY DEFAULT nextval('status_seq'),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL
);

ALTER SEQUENCE status_seq OWNED BY statuses.id;