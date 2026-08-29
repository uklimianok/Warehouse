CREATE SEQUENCE order_pallet_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE order_pallets (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_pallet_seq'),
    order_id BIGINT NOT NULL,
    pallet_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    CONSTRAINT fk_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),
    CONSTRAINT fk_pallet
        FOREIGN KEY (pallet_id)
        REFERENCES pallets(id),
    CONSTRAINT fk_status
        FOREIGN KEY (status_id)
        REFERENCES statuses(id)
);

ALTER SEQUENCE order_pallet_seq OWNED BY order_pallets.id;