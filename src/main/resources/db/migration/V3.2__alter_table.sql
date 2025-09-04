ALTER TABLE member_favorite
    ADD COLUMN total_docs   INT DEFAULT 0,
    ADD COLUMN checked_docs INT DEFAULT 0;
