-- profile_image 테이블 데이터 정리 (필요하다면)
DELETE FROM profile_image;

-- 기존 FK와 컬럼을 제거
ALTER TABLE member
DROP FOREIGN KEY fk_member_profile_image,
    DROP COLUMN profile_image_id;

-- profile_image에 member_id 추가 및 새로운 FK 설정
ALTER TABLE profile_image
    ADD COLUMN member_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_profile_image_member
        FOREIGN KEY (member_id) REFERENCES member(member_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;
