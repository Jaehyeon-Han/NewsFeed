# 개발 환경 세팅 가이드

## GIt Clone

```bash
cd <프로젝트 넣을 폴더>
git clone https://github.com/Jaehyeon-Han/NewsFeed.git
```

## 데이터베이스 연결

- MySQL 접속하여 데이터베이스 생성 (기존 DB도 사용 가능)
- 실행: 구성 → 편집 → 빌드 및 실행 옵션 수정 → 환경 변수에 `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` 입력 후 실행

    ```yaml
    # MySQL DataSource 설정
    datasource:
      url: jdbc:mysql://localhost:3306/${DB_NAME}
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
    ```
- `ddl-auto` 설정은 `create-drop`