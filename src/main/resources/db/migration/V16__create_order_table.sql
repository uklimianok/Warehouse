CREATE SEQUENCE order_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE orders (
    id BIGINT PRIMARY KEY DEFAULT nextval('order_seq'),
    store_id BIGINT NOT NULL,
    gate_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    status_id BIGINT NOT NULL,
    note VARCHAR(255),
    CONSTRAINT fk_store
        FOREIGN KEY (store_id)
        REFERENCES organizations(id),
    CONSTRAINT fk_gate
        FOREIGN KEY (gate_id)
        REFERENCES gates(id),
    CONSTRAINT fk_shift
        FOREIGN KEY (shift_id)
        REFERENCES shifts(id),
    CONSTRAINT fk_status
        FOREIGN KEY (status_id)
        REFERENCES statuses(id)
);

ALTER SEQUENCE order_seq OWNED BY orders.id;