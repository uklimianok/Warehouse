CREATE SEQUENCE organization_type_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE organization_types (
    id BIGINT PRIMARY KEY DEFAULT nextval('organization_type_seq'),
    name VARCHAR(255) UNIQUE NOT NULL
);

ALTER SEQUENCE organization_type_seq OWNED BY organization_types.id;