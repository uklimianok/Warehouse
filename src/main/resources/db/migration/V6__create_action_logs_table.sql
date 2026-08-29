CREATE SEQUENCE action_logs_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE action_logs (
    id BIGINT PRIMARY KEY DEFAULT nextval('action_logs_seq'),
    employee_id BIGINT NOT NULL,
    proceeded_at TIMESTAMP,
    entity_type VARCHAR(255) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(255) NOT NULL,
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
);

ALTER SEQUENCE action_logs_seq OWNED BY action_logs.id;