-- V1: 초기 스키마 생성
-- 작성일: 2025-08-31
-- 작성자: 이동욱

-- 1. 업종 분류 테이블
CREATE TABLE business_class (
                                business_class_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                major_code   VARCHAR(50),
                                major_name   VARCHAR(100),
                                middle_code  VARCHAR(50),
                                middle_name  VARCHAR(100),
                                minor_code   VARCHAR(50),
                                minor_name   VARCHAR(100),
                                created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. 프로필 이미지 테이블
CREATE TABLE profile_image (
                               profile_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               is_deleted  TINYINT(1),
                               storage_key VARCHAR(512),
                               created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. 비즈니스 테이블
CREATE TABLE business (
                          business_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                          business_code_id     BIGINT NOT NULL,
                          business_nm          VARCHAR(100),
                          business_addr        VARCHAR(255),
                          business_addr_detail VARCHAR(255),
                          business_code        VARCHAR(255),
                          business_open_date   DATE,
                          si                   VARCHAR(50),
                          sigungu              VARCHAR(100),
                          eupmyeon_dong        VARCHAR(100),
                          created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_business_class
                              FOREIGN KEY (business_code_id)
                                  REFERENCES business_class(business_class_id)
                                  ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 4. 회원(Member) 테이블
CREATE TABLE member (
                        member_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
                        business_id      BIGINT NOT NULL,
                        profile_image_id BIGINT,
                        member_email     VARCHAR(255),
                        password         VARCHAR(255),
                        member_name      VARCHAR(100),
                        kaka_id          VARCHAR(100),
                        status           ENUM('active','suspended','deleted'),
                        fcm_token        VARCHAR(255),
                        help_count       INT,
                        badge            VARCHAR(512),
                        created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_member_business
                            FOREIGN KEY (business_id) REFERENCES business(business_id)
                                ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT fk_member_profile_image
                            FOREIGN KEY (profile_image_id) REFERENCES profile_image(profile_image_id)
                                ON DELETE SET NULL ON UPDATE CASCADE
);

-- 5. 권한(Auth) 테이블
CREATE TABLE auth (
                      member_auth_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      member_id      BIGINT NOT NULL,
                      auth           VARCHAR(100),
                      created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_auth_member
                          FOREIGN KEY (member_id) REFERENCES member(member_id)
                              ON DELETE CASCADE ON UPDATE CASCADE
);

-- 6. 알림 설정(Alarm Preference)
CREATE TABLE alarm_preference (
                                  alarm_preference_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  member_id        BIGINT NOT NULL,
                                  sos_preference   TINYINT(1),
                                  announce_preference TINYINT(1),
                                  alarm_start_time DATETIME,
                                  alarm_end_time   DATETIME,
                                  is_alarm         TINYINT(1),
                                  created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  UNIQUE KEY uk_alarm_member (member_id),
                                  CONSTRAINT fk_alarm_member
                                      FOREIGN KEY (member_id) REFERENCES member(member_id)
                                          ON DELETE CASCADE ON UPDATE CASCADE
);

-- 7. 공고(Announce)
CREATE TABLE announce (
                          announce_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
                          announce_title  VARCHAR(255),
                          link            VARCHAR(255),
                          author          VARCHAR(50),
                          exc_InsttNm     VARCHAR(50),
                          description     VARCHAR(255),
                          lcategory       VARCHAR(50),
                          pub_date        DATE,
                          reqst_date      DATE,
                          taget_name      VARCHAR(50),
                          view_num        INT,
                          file_path_name  VARCHAR(255),
                          file_name       VARCHAR(255),
                          hashtags        VARCHAR(255),
                          how_to_register VARCHAR(100),
                          call_company    VARCHAR(255),
                          created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 8. 문서(Document)
CREATE TABLE document (
                          document_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          announce_id BIGINT NOT NULL,
                          title       VARCHAR(255),
                          description TEXT,
                          created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_document_announce
                              FOREIGN KEY (announce_id) REFERENCES announce(announce_id)
                                  ON DELETE CASCADE ON UPDATE CASCADE
);

-- 9. 회원 문서 체크(Member_Document_Check)
CREATE TABLE member_document_check (
                                       member_document_check_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       document_id BIGINT NOT NULL,
                                       member_id   BIGINT NOT NULL,
                                       status      TINYINT(1),
                                       checked_at  DATETIME,
                                       created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       UNIQUE KEY uk_mdc_doc_member (document_id, member_id),
                                       CONSTRAINT fk_mdc_document FOREIGN KEY (document_id) REFERENCES document(document_id)
                                           ON DELETE CASCADE ON UPDATE CASCADE,
                                       CONSTRAINT fk_mdc_member FOREIGN KEY (member_id) REFERENCES member(member_id)
                                           ON DELETE CASCADE ON UPDATE CASCADE
);

-- 10. 회원 즐겨찾기(Member_Favorite)
CREATE TABLE member_favorite (
                                 member_favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 announce_id BIGINT NOT NULL,
                                 member_id   BIGINT NOT NULL,
                                 type        ENUM('festival','announce') NOT NULL,
                                 created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 UNIQUE KEY uk_favorite (announce_id, member_id),
                                 CONSTRAINT fk_mf_announce FOREIGN KEY (announce_id) REFERENCES announce(announce_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT fk_mf_member FOREIGN KEY (member_id) REFERENCES member(member_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
);

-- 11. SOS 요청
CREATE TABLE sos (
                     sos_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
                     member_id   BIGINT NOT NULL,
                     sos_type    ENUM('stock','labor','equipment','etc') NOT NULL,
                     sos_content TEXT NOT NULL,
                     expires_at  DATETIME NOT NULL,
                     priority    ENUM('low','normal','high') NOT NULL,
                     is_complete TINYINT(1),
                     is_deleted  TINYINT(1),
                     created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     CONSTRAINT fk_sos_member FOREIGN KEY (member_id) REFERENCES member(member_id)
                         ON DELETE CASCADE ON UPDATE CASCADE
);

-- 12. 채팅방(Chat Room)
CREATE TABLE chat_room (
                           chat_room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           sos_id       BIGINT NOT NULL,
                           room_name    VARCHAR(255),
                           room_type    ENUM('stock','labor','equipment','etc'),
                           priority     ENUM('low','normal','high'),
                           created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_chat_room_sos FOREIGN KEY (sos_id) REFERENCES sos(sos_id)
                               ON DELETE CASCADE ON UPDATE CASCADE
);

-- 13. 채팅 메시지(Chat Message)
CREATE TABLE chat_message (
                              chat_message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              chat_room_id    BIGINT NOT NULL,
                              sender_id       VARCHAR(100),
                              content         TEXT,
                              created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_chat_message_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id)
                                  ON DELETE CASCADE ON UPDATE CASCADE
);

-- 14. 채팅방 상태(Chat Room State)
CREATE TABLE chat_room_state (
                                 chat_room_state_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 member_id    BIGINT NOT NULL,
                                 chat_room_id BIGINT NOT NULL,
                                 last_read_message_id BIGINT,
                                 created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 UNIQUE KEY uk_crs_member_room (member_id, chat_room_id),
                                 CONSTRAINT fk_crs_member FOREIGN KEY (member_id) REFERENCES member(member_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT fk_crs_room FOREIGN KEY (chat_room_id) REFERENCES chat_room(chat_room_id)
                                     ON DELETE CASCADE ON UPDATE CASCADE
);

-- 15. SOS 이미지
CREATE TABLE sos_image (
                           sos_image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           sos_id       BIGINT NOT NULL,
                           storage_key  VARCHAR(512),
                           is_deleted   TINYINT(1),
                           created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_sos_image_sos FOREIGN KEY (sos_id) REFERENCES sos(sos_id)
                               ON DELETE CASCADE ON UPDATE CASCADE
);

-- 16. 공지사항
CREATE TABLE notice (
                        notice_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                        title       VARCHAR(255) NOT NULL,
                        content     TEXT NOT NULL,
                        created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 17. 축제(Festival)
CREATE TABLE festival (
                          festival_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
                          festival_title VARCHAR(255),
                          add1           VARCHAR(255),
                          add2           VARCHAR(255),
                          event_startdate DATE,
                          event_enddate   DATE,
                          first_image    VARCHAR(1024),
                          tel            VARCHAR(50),
                          overview       TEXT,
                          content_id     VARCHAR(60),
                          created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 18. 아티클(Articles)
CREATE TABLE articles (
                          article_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title        VARCHAR(255),
                          content      TEXT,
                          category     VARCHAR(100),
                          author       VARCHAR(100),
                          published_at DATETIME,
                          article_url  VARCHAR(2048),
                          created_at   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 19. 알림(Notification)
CREATE TABLE notification (
                              notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              title       VARCHAR(255),
                              content     TEXT,
                              noti_type   ENUM('all','notice','sos','announce','festival'),
                              is_read     TINYINT(1),
                              created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
