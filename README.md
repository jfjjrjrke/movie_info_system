# 🎬 영화 정보 관리 프로그램

## 📌 프로젝트 소개

사용자가 원한 영화의 정보를 TMDB API로 검색하고, 해당 영화에 대한 상세정보, 리뷰를 확인할 수 있으며, 즐겨찾기로 저장할 수 있는 Java 기반의 영화 정보 관리 시스템입니다.

## 🛠 사용 기술

* Java 8+
* Swing (GUI)
* MySQL (DB)
* JDBC (DB 연동)
* TMDB API

## 📂 프로젝트 구조

```
MovieInfoProject/
├── README.md                   # 설명서 (현재 파일)
├── .gitignore                  # Git 제외 파일 설정
├── movie_db_dump.sql          # DB 전체 복원용 덤프
├── create_tables.sql          # DB 테이블 생성 SQL
├── src/
│   ├── main/                  # 실행 시작점
│   │   └── Main.java
│   ├── gui/                   # GUI 관리 클래스
│   │   ├── MainFrame.java
│   │   ├── MovieTableModel.java
│   │   ├── MovieDetailPanel.java
│   │   ├── ReviewPanel.java
│   │   └── FavoritesPanel.java
│   ├── api/                   # TMDB API 연동
│   │   ├── MovieAPI.java
│   │   ├── ReviewAPI.java
│   │   └── TMDBConfig.java
│   ├── dto/                   # 데이터 전송 객체
│   │   ├── MovieDTO.java
│   │   └── ReviewDTO.java
│   └── dao/                   # DB 처리 클래스
│       ├── MovieDAO.java
│       ├── ReviewDAO.java
│       └── DBUtil.java
```

## ▶ 실행 방법

1. `apikey.properties` 파일 생성 (루트 경로)

```
TMDB_API_KEY=your_tmdb_api_key
```

2. `create_tables.sql` 실행하여 MySQL DB에 테이블 생성

3. Eclipse 또는 IntelliJ에서 프로젝트 실행

   * `Main.java` 실행 시 `MainFrame` 실행됩니다.

4. 영화 검색 → 상세정보 확인 → 즐겨찾기 및 리뷰 확인 가능

### \[DB 설정 방법 - 덕프 파일 기준]

1. MySQL 설치
2. DBeaver 시작 → SQL Editor 열기
3. `movie_db_dump.sql` 열고 전체 실행 (Ctrl+A → Ctrl+Enter)
4. 완료 후 `movie_db` 확인

## 👥 팀원별 역할

| 이름  | 역할                  | 주요 구현 파일                                                                                    |
| --- | ------------------- | ------------------------------------------------------------------------------------------- |
| 김수빈 | GUI 프레임 구성 및 이벤트 제어 | `Main.java`, `MainFrame.java`, `MovieTableModel.java`, `MovieDetailPanel.java`              |
| 최우진 | API 연동 및 DTO 정의     | `MovieAPI.java`, `ReviewAPI.java`, `TMDBConfig.java`, `MovieDTO.java`, `ReviewDTO.java`     |
| 지현우 | DB 연동 및 리뷰/즐겨찾기 구현  | `MovieDAO.java`, `ReviewDAO.java`, `DBUtil.java`, `ReviewPanel.java`, `FavoritesPanel.java` |

## 📌 주의사항

* `apikey.properties` 파일은 Git에 올린이없고, 예시로 `apikey.properties.example` 제공
* JDBC 드라이버가 classpath에 포함되어야 정상 동작
* TMDB API는 하루 호출 수 제한이 있음

---

본 프로젝트는 영화 정보 검색과 저장을 학습 목적으로 구현한 예제이며, 상업적 용도는 아니입니다.