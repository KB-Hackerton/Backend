DELETE FROM announce;
delete from festival;
ALTER TABLE announce AUTO_INCREMENT = 1;

ALTER TABLE announce
    MODIFY COLUMN announce_title VARCHAR(500),
    MODIFY COLUMN link VARCHAR(255),
    MODIFY COLUMN author VARCHAR(100),
    MODIFY COLUMN exc_insttNm VARCHAR(200),
    MODIFY COLUMN description TEXT,
    MODIFY COLUMN lcategory VARCHAR(100),
    MODIFY COLUMN pub_date DATE,
    MODIFY COLUMN reqst_start_date DATE,
    MODIFY COLUMN reqst_end_date DATE,
    MODIFY COLUMN taget_name VARCHAR(200),
    MODIFY COLUMN view_num INT,
    MODIFY COLUMN file_path_name VARCHAR(500),
    MODIFY COLUMN file_name VARCHAR(500),
    MODIFY COLUMN how_to_register TEXT,
    MODIFY COLUMN call_company VARCHAR(1000),
    ADD COLUMN print_file_path_name VARCHAR(500),
    ADD COLUMN print_file_name VARCHAR(200);

alter table announce add unique key uk_pblanc_id (link);