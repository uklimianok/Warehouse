CREATE SEQUENCE organization_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE organizations (
    id BIGINT PRIMARY KEY DEFAULT nextval('organization_seq'),
    name VARCHAR(255) NOT NULL,
    organization_number VARCHAR(255) UNIQUE NOT NULL,
    organization_type_id BIGINT NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
    url VARCHAR(255),
    CONSTRAINT fk_organization_type
        FOREIGN KEY (organization_type_id)
        REFERENCES organization_types(id)
);

ALTER SEQUENCE organization_seq OWNED BY organizations.id;