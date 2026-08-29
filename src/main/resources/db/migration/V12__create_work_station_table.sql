CREATE SEQUENCE work_station_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE work_stations (
    id BIGINT PRIMARY KEY DEFAULT nextval('work_station_seq'),
    station_number VARCHAR(255) NOT NULL,
    control_number VARCHAR(255) NOT NULL,
    type VARCHAR(255),
    workshop_id BIGINT NOT NULL,
    CONSTRAINT fk_workshop
        FOREIGN KEY (workshop_id)
        REFERENCES workshops(id)
);

ALTER SEQUENCE work_station_seq OWNED BY work_stations.id;