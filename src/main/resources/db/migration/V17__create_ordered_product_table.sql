CREATE SEQUENCE ordered_product_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE ordered_products (
    id BIGINT PRIMARY KEY DEFAULT nextval('ordered_product_seq'),
    order_id BIGINT NOT NULL,
    package_id BIGINT NOT NULL,
    ordered_volume DECIMAL(10, 3) NOT NULL,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),
    CONSTRAINT fk_package
        FOREIGN KEY (package_id)
        REFERENCES packages(id)
);

ALTER SEQUENCE ordered_product_seq OWNED BY ordered_products.id;