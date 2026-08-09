INSERT INTO organization_types (id, name) VALUES 
(nextval('organization_type_seq'), 'Employer'),
(nextval('organization_type_seq'), 'Producer');

INSERT INTO organizations (id, name, organization_number, organization_type_id, address, phone_number, email, url) VALUES 
(nextval('organization_seq'),'Organization A', '00000001', 1, 'Address A', '+1234567890', 'A@email.com', 'A.com'),
(nextval('organization_seq'),'Organization B', '00000002', 2, 'Address B', '+1234567890', 'B@email.com', 'B.com');

INSERT INTO positions (id, name, code_name) VALUES 
(nextval('position_seq'), 'Goods Picker', 'GOODS_PICKER'),
(nextval('position_seq'), 'Shift Supervisor', 'SHIFT_SUPERVISOR'),
(nextval('position_seq'), 'Warehouse Employees HR', 'WAREHOUSE_EMPLOYEES_HR'),
(nextval('position_seq'), 'System Administrator', 'SYSTEM_ADMINISTRATOR'),
(nextval('position_seq'), 'Orders Proceeder', 'ORDERS_PROCEEDER');

INSERT INTO shifts (id, symbol) VALUES 
(nextval('shift_seq'), '1');

INSERT INTO employees (id, first_name, last_name, organization_id, employee_number, position_id, shift_id, birth_date, document_id, residence_address, phone_number) VALUES 
(nextval('employee_seq'), 'John', 'Doe', 1, '01000001', 1, 1, '1990-01-01', '123456789', 'Address 01', '+1234567890'),
(nextval('employee_seq'), 'Alex', 'Smith', 1, '02000001', 2, 1, '1990-01-01', '123456789', 'Address 02', '+1234567891'),
(nextval('employee_seq'), 'Bob', 'Brown', 1, '03000001', 3, 1, '1990-01-01', '123456789', 'Address 03', '+1234567892'),
(nextval('employee_seq'), 'Sam', 'Light', 1, '04000001', 4, 1, '1990-01-01', '123456789', 'Address 04', '+1234567893'),
(nextval('employee_seq'), 'Edward', 'Forest', 1, '05000001', 5, 1, '1990-01-01', '123456789', 'Address 05', '+1234567894');

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
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$YrO2j12h8ws0PRxTpvMISOxQE0Hh.6ijYIROuszNMZcP2vv14RudW', true, id
FROM employees WHERE employee_number = '05000001';

INSERT INTO products (id, name, barcode_number, cost, producer_id)
SELECT nextval('products_seq'), 'Mineral Water Polaris 1.5L Carbonated', '1234567890001', 0.69, id
FROM organizations WHERE organization_type_id = 2;