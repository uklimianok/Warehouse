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
    ADD department BIGINT NOT NULL
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES departments(id);