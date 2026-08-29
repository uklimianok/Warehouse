CREATE SEQUENCE paper_card_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE paper_cards (
    id BIGINT PRIMARY KEY DEFAULT nextval('paper_card_seq'),
    code VARCHAR(255) UNIQUE NOT NULL,
    order_pallet_id BIGINT NOT NULL,
    CONSTRAINT fk_order_pallet
        FOREIGN KEY (order_pallet_id)
        REFERENCES order_pallets(id)
);

ALTER SEQUENCE paper_card_seq OWNED BY paper_cards.id;