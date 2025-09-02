ALTER TABLE chat_room
    ADD COLUMN is_complete TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN owner_id BIGINT NOT NULL;

ALTER TABLE member
    RENAME COLUMN kaka_id TO kakao_id;