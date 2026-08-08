INSERT INTO organization_types (id, name) VALUES (nextval('organization_type_seq'), 'Employer');

INSERT INTO organizations (id, name, organization_number, organization_type_id, address, phone, email, url) VALUES (nextval('organization_seq'),'Organization A', '00000001', 1, 'Address A', '+1234567890', 'A@email.com', 'A.com');

INSERT INTO positions (id, name, code_name) VALUES 
(nextval('position_seq'), 'Goods Picker', 'GOODS_PICKER'),
(nextval('position_seq'), 'Shift Supervisor', 'SHIFT_SUPERVISOR'),
(nextval('position_seq'), 'Warehouse Employees HR', 'WAREHOUSE_EMPLOYEES_HR'),
(nextval('position_seq'), 'System Administrator', 'SYSTEM_ADMINISTRATOR');

INSERT INTO shifts (id, symbol) VALUES (nextval('shift_seq'),'1');

INSERT INTO employees (id, first_name, last_name, organization_id, employee_number, position_id, shift_id, birth_date, document_id, residence_address, phone_number) VALUES 
(nextval('employee_seq'), 'John', 'Doe', 1, '01000001', 1, 1, '1990-01-01', '123456789', 'Address 01', '+1234567890'),
(nextval('employee_seq'), 'Alex', 'Smith', 1, '02000001', 2, 1, '1990-01-01', '123456789', 'Address 02', '+1234567891'),
(nextval('employee_seq'), 'Bob', 'Brown', 1, '03000001', 3, 1, '1990-01-01', '123456789', 'Address 03', '+1234567892'),
(nextval('employee_seq'), 'Sam', 'Light', 1, '04000001', 4, 1, '1990-01-01', '123456789', 'Address 04', '+1234567893');

INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, id
FROM employees WHERE employee_number = '01000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, id
FROM employees WHERE employee_number = '02000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, id
FROM employees WHERE employee_number = '03000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, id
FROM employees WHERE employee_number = '04000001';