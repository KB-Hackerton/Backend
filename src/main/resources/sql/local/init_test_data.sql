-- 1. 업종 분류 1건
INSERT INTO business_class (major_code, major_name, middle_code, middle_name, minor_code, minor_name)
VALUES ('M001','제조','MM01','식음료','MN01','제과');

-- 2. 비즈니스 1건
INSERT INTO business (business_code_id, business_nm, business_addr, business_addr_detail, business_code, business_open_date, si, sigungu, eupmyeon_dong)
VALUES (1, '테스트상점', '서울시 어딘가', '101호', 'BIZ-0001', '2024-01-01', '서울', '강남구', '역삼동');

-- 3. 멤버 1건
-- ⚠️ status는 반드시 'active' (enum 소문자) 로!
INSERT INTO member (business_id, member_email, password, member_name, kaka_id, status)
VALUES (1, 'test@local', '{noop}pass', '로컬유저', 'kakao-1', 'active');
