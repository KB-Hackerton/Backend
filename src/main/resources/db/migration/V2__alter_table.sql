ALTER TABLE announce
    ADD COLUMN reqst_start_date DATE     NULL AFTER pub_date,
    ADD COLUMN reqst_end_date   DATE     NULL AFTER reqst_start_date,
    ADD COLUMN map_x            DECIMAL(10,7) NULL AFTER hashtags, -- 경도(longitude)
    ADD COLUMN map_y            DECIMAL(10,7) NULL AFTER map_x,
    DROP COLUMN reqst_date;

ALTER TABLE sos
    ADD COLUMN sos_title VARCHAR(255) NULL AFTER member_id,
    DROP COLUMN priority;

ALTER TABLE chat_room
    DROP COLUMN priority;
