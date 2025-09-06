ALTER TABLE member DROP FOREIGN KEY fk_member_business;
ALTER TABLE member DROP INDEX  fk_member_business;

ALTER TABLE member DROP COLUMN business_id;

ALTER TABLE business
    ADD COLUMN member_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_business_member
        FOREIGN KEY (member_id) REFERENCES member(member_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;
