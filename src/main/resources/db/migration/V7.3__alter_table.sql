
ALTER TABLE notification
    MODIFY COLUMN noti_type ENUM('notice', 'sos', 'announce', 'festival') NULL;

