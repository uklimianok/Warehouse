CREATE SEQUENCE user_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE users (
    id BIGINT PRIMARY KEY DEFAULT nextval('user_seq'),
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    employee_id BIGINT UNIQUE NOT NULL,
    CONSTRAINT fk_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
);

ALTER SEQUENCE user_seq OWNED BY users.id;