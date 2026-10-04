UPDATE positions
    SET controller_flags = jsonb_set(controller_flags, '{OrderPalletController,0}', '"CU"')
    WHERE code_name = 'GOODS_PICKER';