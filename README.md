# 생성형 AI를 활용한 영상 편집 플랫폼 Admin

<h1>관리자 API 문서</h1>

관리자 API에 오신 것을 환영합니다. 이 API는 플랫폼의 관리 기능을 효율적으로 관리하기 위해 설계되었습니다. 이 문서에서는 사용자 관리, 결제, 문의, 공지사항, FAQ 등의 관리를 위한 엔드포인트에 대해 설명합니다.

시작하기
이 API를 사용하기 위해서는 필요한 인증 토큰과 권한이 있어야 합니다. 이 엔드포인트들은 관리자 사용자만을 위한 것이므로 접근 권한이 제한되어 있습니다.

## 사용 기술

Open JDK 17, Spring boot, Gradle, Mybatis, MySql, Restful API, GitHub, SMTP API

## <h2>사용자 관리 (기여도 100%) </h2>

### 가입자 목록 조회
- **엔드포인트**: `GET /admin/join`
- **설명**: 검색 기준에 따른 사용자 목록을 검색합니다.

### 사용자 상세 정보
- **엔드포인트**: `POST /admin/user/details`
- **설명**: 특정 사용자에 대한 상세 정보를 가져옵니다.

### 사용자 삭제
- **엔드포인트**: `PATCH /admin/user`
- **설명**: 사용자를 시스템에서 강제로 삭제합니다.

## 결제 관리

### 결제 목록 조회
- **엔드포인트**: `GET /admin/price`
- **설명**: 검색 기준에 따른 결제 목록을 검색합니다.

### 사용자 상태 업데이트
- **엔드포인트**: `PATCH /admin/price`
- **설명**: 회원의 활동 상태를 업데이트합니다.

## 구독 관리

### 구독 철회 목록 조회
- **엔드포인트**: `GET /admin/pay`
- **설명**: 구독 철회 목록을 검색합니다.

### 구독 철회 상세 정보
- **엔드포인트**: `POST /admin/pay/details`
- **설명**: 사용자의 구독 철회에 대한 상세 정보를 가져옵니다.

## 인보이스 관리

### 인보이스 목록 조회
- **엔드포인트**: `GET /admin/invoice`
- **설명**: 인보이스 목록을 검색합니다.

### 인보이스 상세 정보
- **엔드포인트**: `POST /admin/invoice/details`
- **설명**: 특정 인보이스에 대한 상세 정보를 가져옵니다.

## 문의사항

### 문의사항 목록 조회
- **엔드포인트**: `GET /admin/inquiry`
- **설명**: 문의사항 목록을 검색합니다.

### 문의사항 상세 정보
- **엔드포인트**: `POST /admin/inquiry/details`
- **설명**: 특정 문의사항에 대한 상세 정보를 가져옵니다.

### 문의사항 답변 생성
- **엔드포인트**: `PATCH /admin/inquiry`
- **설명**: 특정 문의사항에 대한 답변을 개시합니다.

## 공지사항

### 공지사항 목록 조회
- **엔드포인트**: `GET /admin/notice`
- **설명**: 공지사항 목록을 검색합니다.

### 공지사항 상세 정보
- **엔드포인트**: `POST /admin/notice/details`
- **설명**: 특정 공지사항에 대한 상세 정보를 가져옵니다.

### 공지사항 생성
- **엔드포인트**: `POST /admin/notice`
- **설명**: 새로운 공지사항을 생성합니다.

### 공지사항 수정
- **엔드포인트**: `PATCH /admin/notice`
- **설명**: 기존의 공지사항을 업데이트합니다.

### 공지사항 삭제
- **엔드포인트**: `DELETE /admin/notice`
- **설명**: 특정 공지사항을 삭제합니다.

## 자주 묻는 질문(FAQ)

### FAQ 목록 조회
- **엔드포인트**: `GET /admin/faq`
- **설명**: FAQ 목록을 검색합니다.

### FAQ 상세 정보
- **엔드포인트**: `POST /admin/faq/details`
- **설명**: 특정 FAQ에 대한 상세 정보를 가져옵니다.

### FAQ 생성
- **엔드포인트**: `POST /admin/faq`
- **설명**: 새로운 FAQ를 생성합니다.

### FAQ 수정
- **엔드포인트**: `PATCH /admin/faq`
- **설명**: 기존의 FAQ를 업데이트합니다.

### FAQ 삭제
- **엔드포인트**: `DELETE /admin/faq`
- **설명**: 특정 FAQ를 삭제합니다.

## 약관 및 조건

### 약관 상세 정보 조회
- **엔드포인트**: `GET /admin/terms`
- **설명**: 약관 및 조건에 대한 상세 정보를 검색합니다.

### 약관 수정
- **엔드포인트**: `PATCH /admin/terms`
- **설명**: 약관 및 조건을 업데이트합니다.

## 추가 기능

### 에디터 이미지 업로드
- **엔드포인트**: `POST /admin/image/upload`
- **설명**: 에디터에서 사용할 이미지를 업로드합니다.

## 인증

### 1차 로그인 토큰 발급
- **엔드포인트**: `POST /admin/login`
- **설명**: 관리자에게 로그인 토큰을 발급합니다.

### 2차 로그인
- **엔드포인트**: `POST /admin/login-ck`
- **설명**: 관리자의 2차 로그인 자격 증명을 검증합니다.

### 첫 2차 로그인 추가 정보 등록
- **엔드포인트**: `PATCH /admin/login-ck`
- **설명**: 첫 2차 로그인 시 추가 정보를 등록합니다.

## 관리자 계정 관리

### 관리자 계정 정보 조회
- **엔드포인트**: `POST /admin/account`
- **설명**: 로그인한 관리자의 계정 정보를 검색합니다.

### 관리자 사용자 정보 업데이트
- **엔드포인트**: `PATCH /admin/account`
- **설명**: 관리자 사용자의 계정 정보를 업데이트합니다.

## 토큰 검증

### 액세스 토큰 검증
- **엔드포인트**: `POST /admin/access-token`
- **설명**: 액세스 토큰의 유효성을 검증합니다.

### 리프레시 토큰 검증
- **엔드포인트**: `POST /admin/refresh-token`
- **설명**: 리프레시 토큰을 검증하고 갱신합니다.

## 로그아웃

### 관리자 로그아웃
- **엔드포인트**: `POST /admin/logout`
- **설명**: 시스템에서 관리자를 로그아웃시킵니다.

## 추가 서비스

### 사용자 비밀번호 초기화
- **엔드포인트**: `POST /admin/user`
- **설명**: 사용자의 비밀번호를 초기화하고 이메일을 통해 알립니다.

### 프로모션 메일 발송
- **엔드포인트**: `POST /admin/send/promotions`
- **설명**: 프로모션 정보를 포함한 메일을 발송합니다.

### 인보이스 메일 발송
- **엔드포인트**: `POST /admin/invoice/email`
- **설명**: 인보이스 정보를 포함한 메일을 발송합니다.


