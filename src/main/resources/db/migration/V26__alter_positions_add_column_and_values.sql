ALTER TABLE positions 
ADD COLUMN department VARCHAR(255);

UPDATE positions
SET department = CASE code_name
    WHEN 'GOODS_UNLOADER' THEN 'Warehouse Employees Department'
    WHEN 'GOODS_PICKER' THEN 'Warehouse Employees Department'
    WHEN 'SET_GOODS_EXPORTER' THEN 'Warehouse Employees Department'
    WHEN 'SET_GOODS_LOADER' THEN 'Warehouse Employees Department'
    WHEN 'OPERATOR' THEN 'Warehouse Employees Department'
    WHEN 'RETURN_GOODS_CONTROLLER' THEN 'Warehouse Employees Department'
    WHEN 'TRUCK_DRIVER' THEN 'Warehouse Employees Department'
    WHEN 'MECHANIC' THEN 'Warehouse Employees Department'
    WHEN 'COORDINATOR' THEN 'Auxiliary Employees Department'
    WHEN 'DATA_CONTROLLER' THEN 'Auxiliary Employees Department'
    WHEN 'SHIFT_SUPERVISOR' THEN 'Auxiliary Employees Department'
    WHEN 'CLEANER' THEN 'Service Department'
    WHEN 'ELECTRICIAN' THEN 'Service Department'
    WHEN 'GENERAL_LABORER' THEN 'Service Department'
    WHEN 'DIRECTOR' THEN 'High-level Staff Department'
    WHEN 'LABOR_PROTECTOR' THEN 'Labor Protection Department'
    WHEN 'MAJOR_HR' THEN 'HR Department'
    WHEN 'WAREHOUSE_EMPLOYEES_HR' THEN 'HR Department'
    WHEN 'OFFICE_EMPLOYEES_HR' THEN 'HR Department'
    WHEN 'MAJOR_ACCOUNTANT' THEN 'Accountancy Department'
    WHEN 'ACCOUNTANT' THEN 'Accountancy Department'
    WHEN 'ORDERS_PROCEEDER' THEN 'Management Department'
    WHEN 'STATISTICS_PROCEEDER' THEN 'Management Department'
    WHEN 'DEVELOPER' THEN 'IT Department'
    WHEN 'SYSTEM_ADMINISTRATOR' THEN 'IT Department'
    ELSE department
END;