INSERT INTO organization_types (id, name) VALUES 
(nextval('organization_type_seq'), 'Employer');

INSERT INTO organizations (id, name, organization_number, organization_type_id, address, phone_number, email, url) VALUES 
(nextval('organization_seq'),'Organization A', '00000001', 1, 'Address A', '+1234567890', 'A@email.com', 'A.com');

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
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '01000001', 1, 1, '1990-01-01', '123456789', 'Address 01', '+1234567890'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '02000001', 2, 1, '1990-01-01', '123456789', 'Address 02', '+1234567891'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '03000001', 3, 1, '1990-01-01', '123456789', 'Address 03', '+1234567892'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '04000001', 4, 1, '1990-01-01', '123456789', 'Address 04', '+1234567893'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '05000001', 5, 1, '1990-01-01', '123456789', 'Address 05', '+1234567894'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '06000001', 6, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '07000001', 7, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '08000001', 8, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '09000001', 9, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '10000001', 10, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '11000001', 11, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '12000001', 12, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '13000001', 13, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '14000001', 14, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '15000001', 15, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '16000001', 16, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '17000001', 17, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '18000001', 18, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '19000001', 19, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '20000001', 20, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '21000001', 21, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '22000001', 22, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '23000001', 23, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895'),
(nextval('employee_seq'), 'FirstName', 'LastName', 1, '24000001', 24, 1, '1990-01-01', '123456789', 'Address 06', '+1234567895');


INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '01000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '02000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '03000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '04000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '05000001';
INSERT INTO users (id, password, enabled, employee_id)
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '06000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '07000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '08000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '09000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '10000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '11000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '12000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '13000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '14000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '15000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '16000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '17000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '18000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '19000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '20000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '21000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '22000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '23000001';
SELECT nextval('user_seq'), 'password', true, id
FROM employees WHERE employee_number = '24000001';