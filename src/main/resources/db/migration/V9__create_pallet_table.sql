CREATE SEQUENCE pallet_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE pallets (
    id BIGINT PRIMARY KEY DEFAULT nextval('pallet_seq'),
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255) NOT NULL,
    length DECIMAL(10, 3) NOT NULL,
    width DECIMAL(10, 3) NOT NULL,
    height DECIMAL(10, 3) NOT NULL,
    weight DECIMAL(10, 3) NOT NULL
);

ALTER SEQUENCE pallet_seq OWNED BY pallets.id;