CREATE SEQUENCE employee_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE employees (
    id BIGINT PRIMARY KEY DEFAULT nextval('employee_seq'),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    employee_organization_id BIGINT NOT NULL,
    employee_number VARCHAR(255) UNIQUE NOT NULL,
    position_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    birth_date DATE,
    document_id VARCHAR(255),
    residence_address VARCHAR(255),
    phone_number VARCHAR(255),
    CONSTRAINT fk_employee_organization
        FOREIGN KEY (employee_organization_id)
        REFERENCES organizations(id),
    CONSTRAINT fk_position
        FOREIGN KEY (position_id)
        REFERENCES positions(id),
    CONSTRAINT fk_shift
        FOREIGN KEY (shift_id)
        REFERENCES shifts(id)
);

ALTER SEQUENCE employee_seq OWNED BY employees.id;