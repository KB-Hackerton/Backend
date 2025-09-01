alter table festival
add constraint unique_content_id unique (content_id);

ALTER TABLE festival
    ADD COLUMN telname VARCHAR(255);