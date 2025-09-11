ALTER TABLE profile_image
    ADD CONSTRAINT uq_member_profile UNIQUE (member_id);
