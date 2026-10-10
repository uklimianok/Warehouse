UPDATE positions
    SET controller_flags = jsonb_set(controller_flags, '{OrderPalletController,0}', '"CRU"')
    WHERE code_name = 'GOODS_PICKER';

UPDATE positions
    SET controller_flags = jsonb_set(controller_flags, '{OrderController,0}', '""')
    WHERE code_name = 'GOODS_PICKER';

UPDATE positions
    SET controller_flags = jsonb_set(controller_flags, '{OrderController,0}', '""')
    WHERE code_name = 'SET_GOODS_EXPORTER';