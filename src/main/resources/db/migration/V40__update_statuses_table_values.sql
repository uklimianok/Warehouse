UPDATE statuses
    SET name = 'Incompleted'
    WHERE id = (SELECT id FROM statuses WHERE name = 'Incomplete' AND type = 'Order');

UPDATE statuses
    SET name = 'Completed'
    WHERE id = (SELECT id FROM statuses WHERE name = 'Complete' AND type = 'Order');

UPDATE statuses 
    SET name = 'Processing' 
    WHERE name = 'Started' AND type = 'Order';