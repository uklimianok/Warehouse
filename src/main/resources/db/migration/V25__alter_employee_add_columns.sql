ALTER TABLE employees 
    ADD COLUMN workshop_id BIGINT,
    ADD COLUMN gate_id BIGINT,
    ADD CONSTRAINT fk_workshop
        FOREIGN KEY (workshop_id)
        REFERENCES workshops(id),
    ADD CONSTRAINT fk_gate
        FOREIGN KEY (gate_id)
        REFERENCES gates(id);
