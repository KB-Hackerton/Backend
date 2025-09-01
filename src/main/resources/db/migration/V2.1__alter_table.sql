ALTER TABLE announce
    DROP COLUMN map_x,
    DROP COLUMN map_y;

ALTER TABLE festival
    ADD COLUMN map_x DECIMAL(10,7) NULL AFTER content_id, -- 경도(longitude)
    ADD COLUMN map_y DECIMAL(10,7) NULL AFTER map_x;
