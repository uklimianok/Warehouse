CREATE TABLE position_inheritances (
    position_id BIGINT NOT NULL,
    inherited_position_id BIGINT NOT NULL,
    CONSTRAINT fk_position
        FOREIGN KEY (position_id)
        REFERENCES positions(id),
    CONSTRAINT fk_inherited_position
        FOREIGN KEY (inherited_position_id)
        REFERENCES positions(id),
    PRIMARY KEY (position_id, inherited_position_id)
);