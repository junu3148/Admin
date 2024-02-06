
-- 그래프
WITH RECURSIVE date_generator AS (
    SELECT CURDATE() - INTERVAL 1 MONTH AS date -- 시작 날짜 설정
UNION ALL
SELECT date - INTERVAL 1 MONTH
FROM date_generator
WHERE date > CURDATE() - INTERVAL 12 MONTH
    )
    , months AS (
SELECT YEAR(date) AS year, MONTH(date) AS month
FROM date_generator
    )
SELECT
    CONCAT(m.year, '년 ', m.month, '월') AS month_formatted,
    COALESCE(COUNT(u.user_key), 0) AS total_signups
FROM months m
         LEFT JOIN user u ON YEAR(u.accession_date) = m.year AND MONTH(u.accession_date) = m.month AND u.is_deleted = 0
GROUP BY m.year, m.month
ORDER BY m.year , m.month;


-- 리플레시 토큰 자정에 자동 삭제
SET GLOBAL event_scheduler = ON;

CREATE EVENT IF NOT EXISTS delete_expired_tokens_07
    ON SCHEDULE EVERY 1 DAY STARTS TIMESTAMP(CURRENT_DATE, '07:00:00')
    DO
DELETE FROM REFRESH_TOKENS WHERE expiry_date < NOW();


CREATE EVENT IF NOT EXISTS delete_expired_tokens_19
    ON SCHEDULE EVERY 1 DAY STARTS TIMESTAMP(CURRENT_DATE, '19:00:00')
    DO
DELETE FROM REFRESH_TOKENS WHERE expiry_date < NOW();

CREATE EVENT IF NOT EXISTS delete_expired_tokens_21
    ON SCHEDULE EVERY 1 DAY STARTS TIMESTAMP(CURRENT_DATE, '21:00:00')
    DO
DELETE FROM REFRESH_TOKENS WHERE expiry_date < NOW();

CREATE EVENT IF NOT EXISTS delete_expired_tokens_00
    ON SCHEDULE EVERY 1 DAY STARTS TIMESTAMP(CURRENT_DATE, '00:00:00')
    DO
DELETE FROM REFRESH_TOKENS WHERE expiry_date < NOW();

-- 조회
SELECT EVENT_NAME, EVENT_SCHEMA, EVENT_DEFINITION, EXECUTE_AT, INTERVAL_VALUE, INTERVAL_FIELD, STATUS
FROM INFORMATION_SCHEMA.EVENTS
WHERE EVENT_NAME = 'delete_expired_tokens_07';





-- 메인 페이지 뷰테이블
CREATE VIEW view_user_activity AS
-- 구독자
SELECT today_count AS userCount,
       CASE
           WHEN today_count > yesterday_count THEN 'up'
           WHEN today_count < yesterday_count THEN 'down'
           ELSE 'stable'
           END AS upDown,
       CONCAT(ROUND(((today_count - yesterday_count) / GREATEST(yesterday_count, 1)) * 100, 0), '%') AS percentage
FROM (SELECT (SELECT COUNT(sub_key)
              FROM subscription
              WHERE sub_date < NOW() AND sub_end_date IS NULL) AS today_count,
             (SELECT COUNT(sub_key)
              FROM subscription
              WHERE sub_date <= DATE_SUB(CURDATE(), INTERVAL 1 DAY)
                AND sub_end_date IS NULL) AS yesterday_count) AS counts

UNION ALL

-- 가입자
SELECT userCount,
       CASE
           WHEN userCount > previous_count THEN 'up'
           WHEN userCount < previous_count THEN 'down'
           ELSE 'stable'
           END AS upDown,
       CONCAT(ROUND(((userCount - previous_count) / previous_count) * 100, 0), '%') AS percentage
FROM (SELECT (SELECT COUNT(user_key) FROM user WHERE accession_date < NOW() AND is_deleted = 0) AS userCount,
             (SELECT COUNT(user_key)
              FROM user
              WHERE accession_date < DATE_SUB(NOW(), INTERVAL 1 DAY)
                AND is_deleted = 0) AS previous_count) AS counts

UNION ALL

-- 구독금액
SELECT userCount,
       CASE
           WHEN userCount > previous_total THEN 'up'
           WHEN userCount < previous_total THEN 'down'
           ELSE 'same'
           END AS upDown,
       CONCAT(ROUND(IF(previous_total = 0, 0, ((userCount - previous_total) / previous_total) * 100)), '%') AS percentage
FROM (SELECT (SELECT SUM(b.plan_price)
              FROM subscription a
                       JOIN plan b ON a.plan_key = b.plan_key
              WHERE YEAR(a.sub_date) = YEAR(CURDATE())
                AND MONTH(a.sub_date) = MONTH(CURDATE())
                AND (a.sub_end_date IS NULL OR a.sub_end_date > CURDATE())
             ) AS userCount,
             (
                 SELECT SUM(b.plan_price)
                 FROM subscription a
                          JOIN plan b ON a.plan_key = b.plan_key
                 WHERE sub_date <= DATE_SUB(CURDATE(), INTERVAL 1 DAY)
                   AND YEAR(a.sub_date) = YEAR(CURDATE())
                   AND MONTH(a.sub_date) = MONTH(CURDATE())
                   AND sub_status = 'Y'
             ) AS previous_total
     ) AS totals

UNION ALL

-- 탈퇴회원
SELECT total_deletions - deletions_up_to_yesterday AS userCount,
       CASE
           WHEN total_deletions > deletions_up_to_yesterday THEN 'up'
           WHEN total_deletions < deletions_up_to_yesterday THEN 'down'
           ELSE 'stable'
           END AS upDown,
       CONCAT(ROUND(((total_deletions - deletions_up_to_yesterday) / GREATEST(deletions_up_to_yesterday, 1)) * 100, 0), '%') AS percentage
FROM (SELECT (SELECT COUNT(user_key) FROM user WHERE is_deleted = 1) AS total_deletions,
             (SELECT COUNT(user_key) FROM user WHERE is_deleted = 1 AND DATE(withdrawal_date) < CURDATE()) AS deletions_up_to_yesterday
     ) AS stats

UNION ALL

-- 구독 해지
SELECT today_ended_subscriptions - yesterday_ended_subscriptions AS userCount,
       CASE
           WHEN today_ended_subscriptions > yesterday_ended_subscriptions THEN 'up'
           WHEN today_ended_subscriptions < yesterday_ended_subscriptions THEN 'down'
           ELSE 'stable'
           END AS upDown,
       CONCAT(ROUND(((today_ended_subscriptions - yesterday_ended_subscriptions) / GREATEST(yesterday_ended_subscriptions, 1)) * 100, 0), '%') AS percentage_change
FROM (SELECT (SELECT COUNT(sub_key)
              FROM subscription
              WHERE MONTH(SUB_END_DATE) = MONTH(CURDATE())
                AND YEAR(SUB_END_DATE) = YEAR(CURDATE())
                AND SUB_END_DATE <= CURDATE()) AS today_ended_subscriptions,
             (
SELECT COUNT(sub_key)
FROM subscription
WHERE MONTH(SUB_END_DATE) = MONTH(CURDATE())
  AND YEAR(SUB_END_DATE) = YEAR(CURDATE())
  AND SUB_END_DATE <= DATE_SUB(CURDATE(), INTERVAL 1 DAY)) AS yesterday_ended_subscriptions
    ) AS counts;



-- 정산페이지 작업중
SELECT
    CONCAT(DATE_FORMAT('2024-01-01', '%Y.%m.%d'), ' ~ ', DATE_FORMAT(LAST_DAY('2024-03-01'), '%Y.%m.%d')) AS 기간,
    SUM(calculate_price) AS 총합계
FROM
    calculate
WHERE
    YEAR(calculate_date) = 2024
  AND MONTH(calculate_date) BETWEEN 1 AND 3;
