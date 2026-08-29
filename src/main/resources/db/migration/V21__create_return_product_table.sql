CREATE SEQUENCE return_product_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE return_products (
    id BIGINT PRIMARY KEY DEFAULT nextval('return_product_seq'),
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    products_amount INT NOT NULL,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

ALTER SEQUENCE return_product_seq OWNED BY return_products.id;