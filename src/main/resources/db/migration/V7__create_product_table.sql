CREATE SEQUENCE product_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE products (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_seq'),
    name VARCHAR(255) NOT NULL,
    barcode_number VARCHAR(255) UNIQUE NOT NULL,
    cost DECIMAL(10, 3) NOT NULL,
    producer_id BIGINT NOT NULL,
    CONSTRAINT producer
        FOREIGN KEY (producer_id)
        REFERENCES organizations(id)
);

ALTER SEQUENCE product_seq OWNED BY products.id;