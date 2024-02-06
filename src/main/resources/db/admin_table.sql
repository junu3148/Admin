--  FOREIGN KEY (admin_key) REFERENCES ADMIN (admin_key) ON DELETE CASCADE 부모키삭제시 자식키 모두삭제(유저에필요)

-- 1. 어드민 테이블
CREATE TABLE ADMIN
(
    admin_key      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    admin_id       VARCHAR(255) NOT NULL UNIQUE,
    admin_password VARCHAR(255) NOT NULL,
    admin_name     VARCHAR(255) NOT NULL,
    depositor      VARCHAR(255) NULL,
    bank_name      VARCHAR(255) NULL,
    account_number VARCHAR(255) NULL,
    role           TINYINT      NOT NULL DEFAULT 0,
    admin_reg_date DATETIME              DEFAULT CURRENT_TIMESTAMP
);

-- 어드민 INSERT
INSERT INTO admin (admin_id, admin_password, admin_name, depositor, bank_name, account_number, role)
VALUES ('menstua@viking-lab.com', '123', '김동규', '김동규', '신한은행', '123-456', 1);

INSERT INTO admin (admin_id, admin_password, admin_name)
VALUES ('test@test.com', '123', '임정민');

-- 2. 공지사항 테이블
-- 공지사항
CREATE TABLE NOTICE
(
    notice_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    notice_title   VARCHAR(255)    NOT NULL,
    notice_content TEXT            NOT NULL,
    notice_date    DATE            NOT NULL,
    notice_hits    INT             NOT NULL DEFAULT 0,
    admin_key      BIGINT UNSIGNED NULL,
    FOREIGN KEY (admin_key) REFERENCES ADMIN (admin_key)
);

-- 공지사항 INSERT
INSERT INTO notice (notice_title, notice_content, notice_date, admin_key)
VALUES ('안녕하세요. LUMEN입니다.', '안녕하세요 LUMEN이 정식 론칭되었습니다. 많은 관심과 사랑 부탁드립니다.', NOW(), 2);


-- 3. 공지사항 이미지 테이블
-- 공지사항 이미지
CREATE TABLE NOTICE_IMAGE
(
    notice_image_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    notice_image     VARCHAR(255)    NOT NULL,
    notice_key       BIGINT UNSIGNED NULL,
    FOREIGN KEY (notice_key) REFERENCES NOTICE (notice_key) ON DELETE CASCADE
);

-- 공지사항 이미지 INSERT
INSERT INTO NOTICE_IMAGE (notice_image, notice_key)
VALUES ('sdg1213fsaddg.jpg', 1);


-- 4. 문의 테이블
create table USER
(
    user_key int not null
);

CREATE TABLE INQUIRY
(
    inquiry_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    inquiry_date    DATE            NOT NULL,
    inquiry_content TEXT            NOT NULL,
    inquiry_status  ENUM ('Y', 'N') NOT NULL DEFAULT 'N',
    qna             TEXT            NULL,
    inquiry_type    VARCHAR(255)    NOT NULL,
    user_key        BIGINT UNSIGNED,
    FOREIGN KEY (user_key) REFERENCES USER (user_key) ON DELETE CASCADE,
    admin_key       BIGINT UNSIGNED,
    FOREIGN KEY (admin_key) REFERENCES ADMIN (admin_key)
);

-- 문의 INSERT
INSERT INTO INQUIRY (inquiry_date, inquiry_content, inquiry_type)
    VALUE (NOW(), '로그인이 안돼요', '로그인');


-- 5. 문의 이미지 테이블
CREATE TABLE INQUIRY_IMAGE
(
    inquiry_image_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    inquiry_image     VARCHAR(255)    NOT NULL,
    inquiry_key       BIGINT UNSIGNED NULL,
    FOREIGN KEY (inquiry_key) REFERENCES INQUIRY (inquiry_key) ON DELETE CASCADE
);

-- 문의 이미지 INSERT
INSERT INTO INQUIRY_IMAGE (inquiry_image, inquiry_key)
VALUES ('sdg1213fsaddg.jpg', 1);


-- 6. FAQ 테이블
CREATE TABLE FAQ
(
    faq_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    faq_title   VARCHAR(255) NOT NULL,
    faq_content TEXT         NOT NULL,
    faq_type    VARCHAR(255) NOT NULL,
    admin_key   BIGINT UNSIGNED,
    FOREIGN KEY (admin_key) REFERENCES ADMIN (admin_key)
);

-- FAQ INSERT
INSERT INTO FAQ (faq_title, faq_content, faq_type, admin_key)
VALUES ('로그인이 안되요 어떻게 하면 될까요?',
        '로그인이 안되는 경우 아래와 같은 스텝을 차분하게 따라오시기 바랍니다. STEP.1 먼저, 브라우저를 확인합니다. 브라우저가 크롬인지, 엣지인지, 사파리인지 확인을 해야 합니다. 그리고...',
        '로그인', 2);


-- 7. 약관 테이블
CREATE TABLE TERMS
(
    terms_key   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    terms_use   TEXT NOT NULL,
    info_policy TEXT NOT NULL
);


-- 8. 정산 테이블
CREATE TABLE CALCULATE
(
    calculate_key   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    calculate_date  DATE NOT NULL,
    calculate_price INT  NOT NULL,
    user_key        BIGINT UNSIGNED,
    FOREIGN KEY (user_key) REFERENCES USER (user_key),
    admin_key       BIGINT UNSIGNED,
    FOREIGN KEY (admin_key) REFERENCES ADMIN (admin_key)
);



-- 9. 리플레시 토큰
CREATE TABLE refresh_tokens
(
    token_key      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    admin_key     BIGINT UNSIGNED NOT NULL,
    refresh_token VARCHAR(1024)   NOT NULL,
    expiry_date   DATETIME        NOT NULL,
    creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_key) REFERENCES Admin (admin_key)
);


