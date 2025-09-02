-- 1) business_class
INSERT IGNORE INTO business_class (business_class_id,major_code,major_name,middle_code,middle_name,minor_code,minor_name,created_at)
       VALUES (1,'G2','소매업','G202','자동차 부품 및 내장품 소매업','G20201','타이어 소매업',NOW());

-- 2) business
INSERT IGNORE INTO business (business_id,business_code_id,business_nm,business_addr,business_addr_detail,business_code,business_open_date,si,sigungu,eupmyeon_dong)
       VALUES (1,1,'동욱타이어','서울특별시 동작구','어딘가에 있을것 같아요','123-123-123-123','2025-09-02','서울특별시','동작구',NULL);

-- 3) profile_image
INSERT IGNORE INTO profile_image (profile_image_id,is_deleted,storage_key,created_at)
       VALUES (1,0,'images/profile/user123.png',NOW());

-- 4) auth
INSERT IGNORE INTO auth (member_auth_id,member_id,auth,created_at)
       VALUES (1,1,'ROLE_Member',NOW());

-- 5) member
INSERT IGNORE INTO member (member_id,business_id,profile_image_id,member_email,password,member_name,status,help_count,badge,created_at)
       VALUES (1,1,1,'test@example.com','$2a$10$8iA3yVSnVBte9Zp60c2OQeXZc.xJhtvGUhx5Db.ZkCjTFscmU1.dy','이동욱','active',10,'도움 촌장',NOW());


ALTER TABLE member_favorite
    DROP COLUMN type;