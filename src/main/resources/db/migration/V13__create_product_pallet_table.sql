CREATE SEQUENCE product_pallet_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE product_pallets (
    id BIGINT PRIMARY KEY DEFAULT nextval('product_pallet_seq'),
    package_id BIGINT NOT NULL,
    package_amount INT NOT NULL,
    pallet_id BIGINT NOT NULL,
    pallet_number VARCHAR(255) NOT NULL,
    group_number VARCHAR(255) NOT NULL,
    status_id BIGINT NOT NULL,
    work_station_id BIGINT NOT NULL,
    next_work_station_id BIGINT NOT NULL,
    CONSTRAINT fk_package
        FOREIGN KEY (package_id)
        REFERENCES packages(id),
    CONSTRAINT fk_pallet
        FOREIGN KEY (pallet_id)
        REFERENCES pallets(id),
    CONSTRAINT fk_status
        FOREIGN KEY (status_id)
        REFERENCES statuses(id),
    CONSTRAINT fk_work_station
        FOREIGN KEY (work_station_id)
        REFERENCES work_stations(id),
    CONSTRAINT fk_next_work_station
        FOREIGN KEY (next_work_station_id)
        REFERENCES work_stations(id)
);

ALTER SEQUENCE product_pallet_seq OWNED BY product_pallets.id;