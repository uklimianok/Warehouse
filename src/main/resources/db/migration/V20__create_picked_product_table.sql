CREATE SEQUENCE picked_product_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE picked_products (
    id BIGINT PRIMARY KEY DEFAULT nextval('picked_product_seq'),
    order_pallet_id BIGINT NOT NULL,
    package_id BIGINT NOT NULL,
    picked_volume DECIMAL(10, 3) NOT NULL,
    CONSTRAINT fk_order_pallet
        FOREIGN KEY (order_pallet_id)
        REFERENCES order_pallets(id),
    CONSTRAINT fk_package
        FOREIGN KEY (package_id)
        REFERENCES packages(id)
);

ALTER SEQUENCE picked_product_seq OWNED BY picked_products.id;