ALTER TABLE product_pallets ALTER COLUMN pallet_id DROP NOT NULL;
ALTER TABLE product_pallets ALTER COLUMN work_station_id DROP NOT NULL;
ALTER TABLE product_pallets ALTER COLUMN next_work_station_id DROP NOT NULL;