-- 1. 플랜 테이블
CREATE TABLE
(
    plan_key   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    plan_name  VARCHAR(255) NOT NULL,
    plan_price INT          NOT NULL,
    plan_type  ENUM('Y', 'M') NULL
);

-- 플랜 INSERT
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Free', 0, null);
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Basic', 15, 'M');
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Pro', 35, 'M');
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Premium', 95, 'M');

INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Basic', 12, 'Y');
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Pro', 28, 'Y');
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Premium', 76, 'Y');
INSERT INTO PLAN (plan_name, plan_price, plan_type)
VALUES ('Enterprise', 855, 'Y');


-- 2. 고객 테이블
CREATE TABLE user
(
    user_key          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id           VARCHAR(255) NOT NULL UNIQUE,
    user_password     VARCHAR(255) NOT NULL,
    user_name         VARCHAR(255) NOT NULL,
    phone_number      VARCHAR(255) NOT NULL,
    accession_date    DATE         NOT NULL,
    withdrawal_date   DATE NULL,
    birth_year        INT NULL,
    occupation        VARCHAR(255) NULL,
    country           VARCHAR(255) NULL,
    gender            ENUM('M', 'F') NULL,
    email_accept      ENUM('Y', 'N') NOT NULL DEFAULT 'N',
    promo_accept      ENUM('Y', 'N') NOT NULL DEFAULT 'N',
    user_status       ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
    out_info          INT          NOT NULL DEFAULT 0,
    sub_round         INT          NOT NULL DEFAULT 0,
    company           VARCHAR(255) NULL,
    plan_key          BIGINT UNSIGNED DEFAULT 1,
    is_deleted        TINYINT(1) NOT NULL DEFAULT 0,
    logo_image        VARCHAR(255) NULL,
    password_recovery DATE         NOT NULL,
    role              VARCHAR(255) NULL,
    FOREIGN KEY (plan_key) REFERENCES PLAN (plan_key)

);

-- 유저 INSERT
INSERT INTO USER (user_id, user_password, user_name, phone_number, accession_date)
VALUES ('menstua@gmail.com', '123', '양준우', '010-1234-5678', NOW());
select *
from user;

-- 3. 구독 테이블
CREATE TABLE SUBSCRIPTION
(
    sub_key      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    sub_date     DATE NOT NULL,
    sub_end_date DATE NULL, -- 디폴트 추가
    sub_status   ENUM('Y', 'N') NOT NULL DEFAULT 'Y',
    user_key     BIGINT UNSIGNED,
    plan_key     BIGINT UNSIGNED,
    FOREIGN KEY (user_key) REFERENCES USER (user_key) ON DELETE CASCADE,
    FOREIGN KEY (plan_key) REFERENCES plan (plan_key)
);

-- 구독 INSERT
INSERT INTO SUBSCRIPTION (sub_date, user_key, plan_key)
VALUES (NOW(), 2, 3);

SELECT a.user_id, b.plan_key, c.plan_name
FROM user a
         INNER JOIN SUBSCRIPTION b ON a.user_key = b.user_key
         INNER JOIN plan c ON b.plan_key = c.plan_key
WHERE a.user_key = 2;


-- 4. 인보이스 테이블
CREATE TABLE INVOICE
(
    invoice_key  BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    invoice_code VARCHAR(255) NOT NULL,
    issue_date   DATE         NOT NULL,
    sub_key      BIGINT UNSIGNED,
    FOREIGN KEY (sub_key) REFERENCES SUBSCRIPTION (sub_key)
);

-- 인보이스 INSERT
INSERT INTO INVOICE(invoice_code, issue_date, sub_key)
VALUES ('A23KM53LIN2374MK', NOW(), 1);


-- 5. 이메일인증 테이블
CREATE TABLE EMAIL_AUTH
(
    email_auth_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    auth_email     VARCHAR(255) NOT NULL,
    auth_code      INT          NOT NULL,
    auth_status    ENUM('Y','N') NOT NULL DEFAULT 'N'
);

-- 이메일인증 INSERT
INSERT INTO EMAIL_AUTH(auth_email, auth_code)
VALUES ('test@test.com', '12345678');


-- 6. 간편로그인 테이블
CREATE TABLE EASY_LOGIN
(
    easy_login_key   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    easy_login_email VARCHAR(255) NOT NULL,
    easy_login_name  VARCHAR(255) NOT NULL
);

-- 간편로그인 INSERT
INSERT INTO EASY_LOGIN(easy_login_email, easy_login_name)
VALUES ('test@test.com', '양준우');


-- 7. 카드 테이블
CREATE TABLE CADE
(
    cade_key         BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cade_number      VARCHAR(255) NOT NULL,
    valid_date       DATE         NOT NULL,
    cvc              VARCHAR(255) NOT NULL,
    cade_name        VARCHAR(255) NOT NULL,
    city_province    VARCHAR(255) NOT NULL,
    basic_address    VARCHAR(255) NOT NULL,
    detailed_address VARCHAR(255) NOT NULL,
    postal_code      VARCHAR(255) NOT NULL,
    statement_email  VARCHAR(255) NOT NULL,
    vat_id           VARCHAR(255) NULL,
    country          VARCHAR(255) NOT NULL,
    user_key         BIGINT UNSIGNED,
    FOREIGN KEY (user_key) REFERENCES USER (user_key)

);

INSERT INTO CADE (cade_number, valid_date, cvc, cade_name, city_province, basic_address, detailed_address, postal_code,
                  statement_email, vat_id, country, user_key)
VALUES ('123', '2024-10-22', '123', 'SS', 'DD', 'DD', 'DD', 'DD', 'DD', 'DD', 'DD', 'DD');

-- 8. 결제 테이블
CREATE TABLE PAYMENT
(
    payment_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    cade_key    BIGINT UNSIGNED,
    FOREIGN KEY (cade_key) REFERENCES CADE (cade_key)
);


-- 9. 제휴문의 테이블
CREATE TABLE AFFILIATE_INQUIRY
(
    affiliate_inquiry_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    applicant             VARCHAR(255) NOT NULL,
    applicant_email       VARCHAR(255) NOT NULL,
    affiliation           VARCHAR(255) NOT NULL,
    title                 VARCHAR(255) NOT NULL,
    content               TEXT         NOT NULL
);