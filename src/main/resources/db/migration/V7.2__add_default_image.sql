ALTER TABLE `profile_image`
  DROP COLUMN `is_deleted`,
  MODIFY `storage_key` varchar(512) NOT NULL DEFAULT 'https://kb-image-s3.s3.ap-northeast-2.amazonaws.com/default_logo.png';
