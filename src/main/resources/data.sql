INSERT INTO organization_types (id, name) VALUES (nextval('organization_type_seq'),'Employer');
INSERT INTO organizations (id, name, organization_number, organization_type_id, address, phone, email, url) VALUES (nextval('organization_seq'),'Organization A', '00000001', 1, 'Address A', '+1234567890', 'A@email.com', 'A.com');
INSERT INTO positions (id, name, code_name) VALUES 
(nextval('position_seq'), 'Director', 'DIRECTOR'),
(nextval('position_seq'), 'Goods picker', 'GOODS_PICKER');
INSERT INTO shifts (id, symbol) VALUES (nextval('shift_seq'),'1');
INSERT INTO employees (id, first_name, last_name, organization_id, employee_number, position_id, shift_id, birth_date, document_id, residence_address, phone_number) VALUES (nextval('employee_seq'), 'John', 'Doe', 1, '10000001', 1, 1, '1990-01-01', '123456789', 'Address 01', '+1234567890');
INSERT INTO users (id, password, enabled, employee_id) VALUES (nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, 1);