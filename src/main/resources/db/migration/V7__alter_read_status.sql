

ALTER TABLE chat_message
    MODIFY COLUMN sender_id BIGINT NULL,
    ADD CONSTRAINT fk_chat_message_sender
        FOREIGN KEY (sender_id) REFERENCES member (member_id)
        ON UPDATE CASCADE
           ON DELETE SET NULL;

CREATE TABLE IF NOT EXISTS read_status (
                                           read_status_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           chat_room_id BIGINT NOT NULL,
                                           chat_message_id BIGINT NOT NULL,
                                           member_id BIGINT NOT NULL,
                                           is_read BOOLEAN DEFAULT FALSE,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

