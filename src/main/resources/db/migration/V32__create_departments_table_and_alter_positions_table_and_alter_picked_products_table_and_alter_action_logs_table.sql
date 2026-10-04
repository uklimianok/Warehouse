CREATE SEQUENCE department_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE departments (
    id BIGINT PRIMARY KEY DEFAULT nextval('department_seq'),
    name VARCHAR(255) UNIQUE NOT NULL,
    code_name VARCHAR(255) UNIQUE NOT NULL,
    priority INT NOT NULL,
    is_removable BOOLEAN NOT NULL DEFAULT false
);

ALTER SEQUENCE department_seq OWNED BY departments.id;

ALTER TABLE positions
    RENAME COLUMN has_database_access TO is_enabled;

ALTER TABLE positions
    DROP COLUMN department;

ALTER TABLE positions
    ADD is_removable BOOLEAN NOT NULL DEFAULT false,
    ADD controller_flags JSONB NOT NULL DEFAULT '{}'::jsonb,
    ADD department_id BIGINT,
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id);

CREATE INDEX idx_positions_controller_flags ON positions USING GIN (controller_flags);

ALTER TABLE picked_products
    ADD is_completed BOOLEAN NOT NULL

ALTER TABLE action_logs
    DROP COLUMN employee_id;

ALTER TABLE action_logs
    add COLUMN employee_number VARCHAR(255) NOT NULL DEFAULT '';