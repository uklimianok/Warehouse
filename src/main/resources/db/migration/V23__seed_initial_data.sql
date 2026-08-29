INSERT INTO organization_types (id, name) VALUES 
(nextval('organization_type_seq'), 'Employer');

INSERT INTO organizations (id, name, organization_number, organization_type_id, address, phone_number, email, url) VALUES 
(nextval('organization_seq'), 'Organization A', '00000001', (SELECT id FROM organization_types WHERE name = 'Employer'), 'Address A', '+1234567890', 'A@email.com', 'A.com');

INSERT INTO positions (id, name, code_name, has_database_access) VALUES 
(nextval('position_seq'), 'Truck Driver', 'TRUCK_DRIVER', false),
(nextval('position_seq'), 'Goods Unloader', 'GOODS_UNLOADER', true),
(nextval('position_seq'), 'Goods Picker', 'GOODS_PICKER', true),
(nextval('position_seq'), 'Set Goods Exporter', 'SET_GOODS_EXPORTER', true),
(nextval('position_seq'), 'Set Goods Loader', 'SET_GOODS_LOADER', true),
(nextval('position_seq'), 'Operator', 'OPERATOR', true),
(nextval('position_seq'), 'Return Goods Controller', 'RETURN_GOODS_CONTROLLER', true),
(nextval('position_seq'), 'Coordinator', 'COORDINATOR', true),
(nextval('position_seq'), 'Data Controller', 'DATA_CONTROLLER', true),
(nextval('position_seq'), 'Shift Supervisor', 'SHIFT_SUPERVISOR', true),
(nextval('position_seq'), 'Mechanic', 'MECHANIC', false),
(nextval('position_seq'), 'Director', 'DIRECTOR', true),
(nextval('position_seq'), 'Major HR', 'MAJOR_HR', true),
(nextval('position_seq'), 'Warehouse Employees HR', 'WAREHOUSE_EMPLOYEES_HR', true),
(nextval('position_seq'), 'Office Employees HR', 'OFFICE_EMPLOYEES_HR', true),
(nextval('position_seq'), 'Cleaner', 'CLEANER', false),
(nextval('position_seq'), 'Electric', 'ELECTRIC', false),
(nextval('position_seq'), 'General Laborer', 'GENERAL_LABORER', false),
(nextval('position_seq'), 'Orders Proceeder', 'ORDERS_PROCEEDER', true),
(nextval('position_seq'), 'Statistics Proceeder', 'STATISTICS_PROCEEDER', true),
(nextval('position_seq'), 'Major Accountant', 'MAJOR_ACCOUNTANT', false),
(nextval('position_seq'), 'Accountant', 'ACCOUNTANT', false),
(nextval('position_seq'), 'Developer', 'DEVELOPER', true),
(nextval('position_seq'), 'System Administrator', 'SYSTEM_ADMINISTRATOR', true),
(nextval('position_seq'), 'Labor Protector', 'LABOR_PROTECTOR', false);

INSERT INTO shifts (id, symbol) VALUES 
(nextval('shift_seq'), '1');

INSERT INTO employees (id, first_name, last_name, employer_organization_id, employee_number, position_id, shift_id, birth_date, document_id, residence_address, phone_number) VALUES 
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '01000001', (SELECT id FROM positions WHERE code_name = 'TRUCK_DRIVER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '02000001', (SELECT id FROM positions WHERE code_name = 'GOODS_UNLOADER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '03000001', (SELECT id FROM positions WHERE code_name = 'GOODS_PICKER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '04000001', (SELECT id FROM positions WHERE code_name = 'SET_GOODS_EXPORTER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '05000001', (SELECT id FROM positions WHERE code_name = 'SET_GOODS_LOADER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '06000001', (SELECT id FROM positions WHERE code_name = 'OPERATOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '07000001', (SELECT id FROM positions WHERE code_name = 'RETURN_GOODS_CONTROLLER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '08000001', (SELECT id FROM positions WHERE code_name = 'COORDINATOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '09000001', (SELECT id FROM positions WHERE code_name = 'DATA_CONTROLLER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '10000001', (SELECT id FROM positions WHERE code_name = 'SHIFT_SUPERVISOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '11000001', (SELECT id FROM positions WHERE code_name = 'MECHANIC'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '12000001', (SELECT id FROM positions WHERE code_name = 'DIRECTOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '13000001', (SELECT id FROM positions WHERE code_name = 'MAJOR_HR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '14000001', (SELECT id FROM positions WHERE code_name = 'WAREHOUSE_EMPLOYEES_HR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '15000001', (SELECT id FROM positions WHERE code_name = 'OFFICE_EMPLOYEES_HR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '16000001', (SELECT id FROM positions WHERE code_name = 'CLEANER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '17000001', (SELECT id FROM positions WHERE code_name = 'ELECTRIC'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '18000001', (SELECT id FROM positions WHERE code_name = 'GENERAL_LABORER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '19000001', (SELECT id FROM positions WHERE code_name = 'ORDERS_PROCEEDER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '20000001', (SELECT id FROM positions WHERE code_name = 'STATISTICS_PROCEEDER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '21000001', (SELECT id FROM positions WHERE code_name = 'MAJOR_ACCOUNTANT'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '22000001', (SELECT id FROM positions WHERE code_name = 'ACCOUNTANT'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '23000001', (SELECT id FROM positions WHERE code_name = 'DEVELOPER'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '24000001', (SELECT id FROM positions WHERE code_name = 'SYSTEM_ADMINISTRATOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', (SELECT id FROM organizations WHERE organization_number = '00000001'), '25000001', (SELECT id FROM positions WHERE code_name = 'LABOR_PROTECTOR'), (SELECT id FROM shifts WHERE symbol = '1'), '1990-01-01', '123456789', 'Address 01', '+1234567895');

INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '01000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '02000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '03000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '04000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '05000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '06000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '07000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '08000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '09000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '10000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '11000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '12000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '13000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '14000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '15000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '16000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '17000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '18000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '19000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '20000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '21000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '22000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '23000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), '$2a$10$zLoD5QL9ULMjbL0paai5nerNUh1Y4B6Plx5sIzXATGIWYHI2ajXqa', true, id
FROM employees WHERE employee_number = '24000001';