-- sos 테이블에서 is_deleted 컬럼 삭제
ALTER TABLE sos
DROP COLUMN is_deleted;

-- sos_image 테이블에서 is_deleted 컬럼 삭제
ALTER TABLE sos_image
DROP COLUMN is_deleted;
