CREATE SEQUENCE shift_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE shifts (
    id BIGINT PRIMARY KEY DEFAULT nextval('shift_seq'),
    symbol VARCHAR(255) UNIQUE NOT NULL
);

ALTER SEQUENCE shift_seq OWNED BY shifts.id;