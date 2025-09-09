alter table notification
add column member_id BIGINT NOT NULL AFTER notification_id;

ALTER TABLE notification
    ADD CONSTRAINT fk_notification_member
        FOREIGN KEY (member_id) REFERENCES member (member_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE;