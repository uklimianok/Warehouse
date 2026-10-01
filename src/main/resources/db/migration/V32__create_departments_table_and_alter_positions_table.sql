CREATE SEQUENCE department_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE departments (
    id BIGINT PRIMARY KEY DEFAULT nextval('department_seq'),
    name VARCHAR(255) UNIQUE NOT NULL,
    code_name VARCHAR(255) UNIQUE NOT NULL,
    priority INT NOT NULL
);

ALTER SEQUENCE department_seq OWNED BY departments.id;

ALTER TABLE positions
    RENAME COLUMN has_database_access TO is_enabled;

ALTER TABLE positions
    DROP COLUMN department;

ALTER TABLE positions
    ADD is_removable BOOLEAN NOT NULL
    ADD controller_flags JSONB NOT NULL DEFAULT '{}'::jsonb
    ADD department_id BIGINT NOT NULL
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id);

CREATE INDEX idx_positions_controller_flags ON positions USING GIN (controller_flags);

UPDATE positions
    SET is_removable = CASE code_name
        WHEN 'GOODS_UNLOADER' THEN false
        WHEN 'GOODS_PICKER' THEN false
        WHEN 'SET_GOODS_EXPORTER' THEN false
        WHEN 'SET_GOODS_LOADER' THEN false
        WHEN 'OPERATOR' THEN false
        WHEN 'RETURN_GOODS_CONTROLLER' THEN false
        WHEN 'TRUCK_DRIVER' THEN false
        WHEN 'MECHANIC' THEN false
        WHEN 'COORDINATOR' THEN false
        WHEN 'DATA_CONTROLLER' THEN false
        WHEN 'SHIFT_SUPERVISOR' THEN false
        WHEN 'CLEANER' THEN false
        WHEN 'ELECTRICIAN' THEN false
        WHEN 'GENERAL_LABORER' THEN false
        WHEN 'DIRECTOR' THEN false
        WHEN 'LABOR_PROTECTOR' THEN false
        WHEN 'MAJOR_HR' THEN false
        WHEN 'WAREHOUSE_EMPLOYEES_HR' THEN false
        WHEN 'OFFICE_EMPLOYEES_HR' THEN false
        WHEN 'MAJOR_ACCOUNTANT' THEN false
        WHEN 'ACCOUNTANT' THEN false
        WHEN 'ORDERS_PROCEEDER' THEN false
        WHEN 'STATISTICS_PROCEEDER' THEN false
        WHEN 'DEVELOPER' THEN false
        WHEN 'SYSTEM_ADMINISTRATOR' THEN false
    ELSE is_removable
END;