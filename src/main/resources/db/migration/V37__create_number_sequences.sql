CREATE SEQUENCE employee_number_seq;
CREATE SEQUENCE pallet_number_seq;

-- Start each sequence after the highest number already in the table
SELECT setval('employee_number_seq',
    COALESCE((SELECT MAX(employee_number::BIGINT) FROM employees), 0) + 1,
    false);

SELECT setval('pallet_number_seq',
    COALESCE((SELECT MAX(pallet_number::BIGINT) FROM product_pallets), 0) + 1,
    false);