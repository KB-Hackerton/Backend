
-- 1) member
INSERT IGNORE INTO member (member_id,business_id,profile_image_id,member_email,password,member_name,status,help_count,badge,created_at)
       VALUES (1,1,1,'test@example.com','$2a$10$8iA3yVSnVBte9Zp60c2OQeXZc.xJhtvGUhx5Db.ZkCjTFscmU1.dy','이동욱','active',10,'도움 촌장',NOW());

-- 2) auth
INSERT IGNORE INTO auth (member_auth_id,member_id,auth,created_at)
       VALUES (1,1,'ROLE_Member',NOW());
