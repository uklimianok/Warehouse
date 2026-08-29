CREATE SEQUENCE workshop_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE workshops (
    id BIGINT PRIMARY KEY DEFAULT nextval('workshop_seq'),
    name VARCHAR(255) NOT NULL,
    standard DECIMAL(10, 3) NOT NULL
);

ALTER SEQUENCE workshop_seq OWNED BY workshops.id;