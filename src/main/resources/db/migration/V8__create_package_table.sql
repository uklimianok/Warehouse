CREATE SEQUENCE package_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE packages (
    id BIGINT PRIMARY KEY DEFAULT nextval('package_seq'),
    product_id BIGINT NOT NULL,
    products_amount INT NOT NULL,
    volume DECIMAL(10, 3) NOT NULL,
    weight DECIMAL(10, 3) NOT NULL,
    CONSTRAINT fk_package
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

ALTER SEQUENCE package_seq OWNED BY packages.id;