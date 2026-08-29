CREATE SEQUENCE track_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE tracks (
    id BIGINT PRIMARY KEY DEFAULT nextval('track_seq'),
    symbol VARCHAR(255) UNIQUE NOT NULL,
    length DECIMAL(10, 3) NOT NULL,
    width DECIMAL(10, 3) NOT NULL,
    gate_id BIGINT NOT NULL,
    CONSTRAINT fk_gate
        FOREIGN KEY (gate_id)
        REFERENCES gates(id)
);

ALTER SEQUENCE track_seq OWNED BY tracks.id;