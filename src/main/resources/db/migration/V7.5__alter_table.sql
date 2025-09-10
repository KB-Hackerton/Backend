ALTER TABLE chat_room_state
    MODIFY COLUMN last_read_message_id BIGINT NOT NULL DEFAULT 0;
