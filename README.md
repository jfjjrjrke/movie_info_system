# 🎮 영화 정보 관리 프로그램

## 📌 프로젝트 소개

TMDB API를 활용하여 영화 정보를 검색하고, 상세정보와 리뷰를 확인하고, 즐겨찾기로 저장할 수 있는 **Java 기반의 영화 정보 관리 시스템**입니다. GUI 기반의 직관적인 인터페이스와 MySQL 연동을 통해 사용자가 손쉽게 영화 데이터를 관리할 수 있도록 설계되어 있습니다.

## 💪 사용 기술

* Java 8+
* Swing (GUI)
* MySQL (DB)
* JDBC (DB 연동)
* TMDB API

## 📂 프로젝트 구조

```
MovieInfoProject/
├── README.md                    # 설명서
├── .gitignore                   # Git 제외 파일 설정
├── movie_db_dump.sql           # 전체 데이터 덤프
├── create_tables.sql           # 테이블 생성 SQL
├── db.properties               # DB 연결 설정 (업로드 금지)
├── db.properties.example       # DB 설정 예시
├── apikey.properties           # TMDB API 키 설정 (업로드 금지)
├── apikey.properties.example   # TMDB API 키 예시
├── src/
│   ├── main/
│   │   └── Main.java
│   ├── movie_info_system/
│   │   ├── gui/                # GUI 구성
│   │   │   ├── MainFrame.java
│   │   │   ├── MovieDetailPanel.java
│   │   │   ├── ReviewPanel.java
│   │   │   └── FavoritesPanel.java
│   │   ├── api/                # API 연동
│   │   │   ├── MovieAPI.java
│   │   │   ├── ReviewAPI.java
│   │   │   └── TMDBConfig.java
│   │   ├── dto/                # 데이터 객체
│   │   │   ├── MovieDTO.java
│   │   │   ├── ReviewDTO.java
│   │   │   └── FavoriteDTO.java
│   │   └── dao/                # DB 연동
│   │       ├── MovieDAO.java
│   │       ├── ReviewDAO.java
│   │       ├── FavoriteDAO.java
│   │       └── DBUtil.java
│   └── test/
│       └── DBTest.java
├── resources/                  # TMDB 로고 이미지 등 리소스
│   └── assets/
│       └── logo_tmdb.png
```

## ▶ 실행 방법

### ✅ API 설정

1. `apikey.properties.example` → `apikey.properties`로 복사
2. 다음과 같이 TMDB API 키 입력

```properties
TMDB_API_KEY=your_tmdb_api_key
```

### ✅ DB 설정

1. `db.properties.example` → `db.properties`로 복사
2. DB 접속 정보 입력

```properties
db.url=jdbc:mysql://localhost:3306/movie_db
db.user=root
db.password=your_password
```

3. `create_tables.sql` 또는 `movie_db_dump.sql`로 DB 생성
4. Eclipse에서 `Main.java` 실행 → GUI 시작

### ✅ DB 연결 테스트

```java
try (Connection conn = DBUtil.getConnection()) {
    System.out.println(conn != null ? "✅ DB 연결 성공!" : "❌ DB 연결 실패!");
} catch (Exception e) {
    System.out.println("⛔ 예외 발생: " + e.getMessage());
}
```

## 📁 리소스 설정 안내 (TMDB 로고 표시용)

본 프로젝트에서는 TMDB 로고 이미지를 표시하기 위해 `resources/assets/logo_tmdb.png` 파일을 사용합니다.
해당 이미지가 정상적으로 로드되기 위해서는 **`resources/` 폴더를 소스 경로(Build Path)로 추가**해야 합니다.

### 🔧 Eclipse 설정 방법

1. 프로젝트 내 `resources/` 폴더를 마우스 오른쪽 클릭
2. `Build Path` → `Use as Source Folder` 선택

### ✅ 리소스 접근 경로

```java
getClass().getResource("/assets/logo_tmdb.png")
```

해당 경로는 `.jar` 빌드 시에도 유지되며, TMDB 로고와 출처 문구가 GUI 하단에 정상 표시됩니다.

---

## 👥 팀원별 역할 분담

| 이름  | 역할 요약                                        | 구현 파일                                                                                                       |
| --- | -------------------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| 김수빈 | GUI 시각적 구성 (화면 배치 및 레이아웃)                    | `MainFrame.java`, `ReviewPanel.java`, `FavoritesPanel.java`, `MovieDetailPanel.java`                        |
| 지현우 | DB 연동 및 이벤트 처리, 비즈니스 로직 구현 (GUI 구성요소와 기능 연동) | `MainFrame.java`, `MovieDAO.java`, `ReviewDAO.java`, `FavoriteDAO.java`, `DBUtil.java`                      |
| 최우진 | TMDB API 연동 및 DTO 정의                         | `MovieAPI.java`, `ReviewAPI.java`, `TMDBConfig.java`, `MovieDTO.java`, `ReviewDTO.java`, `FavoriteDTO.java` |

※ GUI는 김수빈이 초기 구성하였고, 그 안의 이벤트 처리 (검색, 즐겨찾기, 리뷰 저장 등)와 DB 호출, 데이터 연동은 지현우가 구현하였습니다.

---

## ✅ 주요 기능 설명

| 기능      | 설명                                            |
| ------- | --------------------------------------------- |
| 영화 검색   | TMDB API + DB를 활용한 영화 통합 검색 결과 표시             |
| 영화 상세보기 | 영화 선택 시 상세정보 표시 (포스터, 개요, 평점, 출시일 등)          |
| 즐겨찾기    | 즐겨찾기 등록/삭제 및 목록 표시 (DB에 저장)                   |
| 리뷰 관리   | TMDB 리뷰 + 직접 작성한 리뷰 통합 표시, 내가 쓴 리뷰는 DB에 저장 가능 |

## 🔄 협업 & 데이터 흐름

| 항목               | 제공자 → 수신자      | 설명                       |
| ---------------- | -------------- | ------------------------ |
| DTO (`MovieDTO`) | API → GUI/DB   | JSON 응답을 DTO로 변환해 전달     |
| DAO 호출 입력        | GUI → DB       | 영화 ID, 리뷰 내용 등 전달        |
| `.properties` 설정 | 팀원 간 공유        | 개인 환경에 맞게 복사/수정          |
| API 응답 형식        | API 담당 → 전체 공유 | 샘플 JSON 구조 제공 및 연동 형식 통일 |

---

## ☁️ 클라우드 DB 전환 계획

* 현재: 로컬 MySQL 사용 중
* 계획: **AWS RDS** 기반의 클라우드 환경으로 전환 예정

  * `db.properties` 파일만 수정하면 적용 가능
  * IP 접근 제한, 백업, 모니터링 등을 고려한 배포

---

## ⚠️ 주의사항

* `apikey.properties`, `db.properties`는 Git에 업로드 금지
* `.gitignore`에 반드시 포함
* TMDB API 호출은 제한이 있으므로 과도한 호출 주의
* JDBC 드라이버는 classpath에 반드시 포함되어야 실행 가능

---

본 프로젝트는 교육 및 학습 목적의 예제이며, 상업적 용도로 사용되지 않습니다.
