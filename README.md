# 🎬 영화 정보 관리 프로그램

## 📌 프로젝트 소개

사용자가 원하는 영화의 정보를 TMDB API로 검색하고, 해당 영화에 대한 상세정보, 리뷰를 확인할 수 있고, 즐겨찾기로 저장할 수 있는 Java 기반의 영화 정보 관리 시스템입니다.

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
├── db.properties              # 실제 DB 연결 설정 파일 (Git에는 업로드 안함)
├── db.properties.example      # DB 연결 예시 설정 파일 (GitHub에 포함)
├── apikey.properties           # TMDB API 키 설정 파일 (Git에는 업로드 안함)
├── apikey.properties.example   # TMDB API 예시 설정 파일 (GitHub에 포함)
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
│   │   ├── ReviewDTO.java
│   │   └── FavoriteDTO.java
│   ├── dao/                   # DB 처리 클래스
│   │   ├── MovieDAO.java
│   │   ├── ReviewDAO.java
│   │   ├── FavoriteDAO.java
│   │   └── DBUtil.java
│   └── test/                  # 테스트 코드
│       └── DBTest.java        # DB 연결 테스트용 클래스
```

## ▶ 실행 방법

### ✅ API 설정 방법

1. `apikey.properties.example` 파일을 복사하여 `apikey.properties`로 변경
2. 해당 파일에 TMDB API 키 입력

```properties
TMDB_API_KEY=your_tmdb_api_key
```

### ✅ DB 설정 방법

1. `db.properties.example` 파일을 복사하여 `db.properties`로 변경 (Git에는 업로드하지 않음)
2. 로컬 또는 클라우드 DB 접속 정보 입력

```properties
db.url=jdbc:mysql://localhost:3306/movie_db
db.user=root
db.password=your_password
```

3. `create_tables.sql` 또는 `movie_db_dump.sql`을 DBeaver에서 실행하여 테이블 구조 생성 또는 전체 복원
4. Eclipse 또는 IntelliJ에서 `Main.java` 실행 → `MainFrame` 실행됨
5. 영화 검색 → 상세정보 확인 → 즐겨찾기 및 리뷰 확인 가능

### ✅ DB 연결 테스트 방법

* `src/test/DBTest.java` 실행

```java
import java.sql.Connection;
import dao.DBUtil;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null) {
                System.out.println("✅ DB 연결 성공!");
            } else {
                System.out.println("❌ DB 연결 실패!");
            }
        } catch (Exception e) {
            System.out.println("❌ 예외 발생: " + e.getMessage());
        }
    }
}
```

## 👥 팀원별 역할 및 주요 구현 기능 (상세)

| 이름  | 역할                                 | 주요 구현 파일                                                                                                                  |
| --- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| 김수빈 | GUI 프레임 구성 및 이벤트 제어, 리뷰/즐겨찾기 패널 구현 | `Main.java`, `MainFrame.java`, `MovieTableModel.java`, `MovieDetailPanel.java`, `ReviewPanel.java`, `FavoritesPanel.java` |
| 최우진 | API 연동 및 DTO 정의                    | `MovieAPI.java`, `ReviewAPI.java`, `TMDBConfig.java`, `MovieDTO.java`, `ReviewDTO.java`, `FavoriteDTO.java`               |
| 지현우 | DB 연동 및 DAO 구현                     | `MovieDAO.java`, `ReviewDAO.java`, `FavoriteDAO.java`, `DBUtil.java`                                                      |

---

## ✅ GUI 구성 요소별 기능 설명 (김수빈 담당)

| UI 구성 요소                      | 기능 설명                                                                    |
| ----------------------------- | ------------------------------------------------------------------------ |
| 🔍 검색창 (JTextField + JButton) | 키워드 입력 후 버튼 클릭 시 `MovieAPI.searchMovies()` 호출 → `MovieTableModel`에 결과 표시 |
| 🎬 영화 상세정보 패널                 | 테이블에서 영화 선택 시 상세정보 조회 후 출력                                               |
| ⭐ 즐겨찾기 버튼                     | 선택된 영화 정보를 즐겨찾기로 DB에 저장 (`MovieDAO.addToFavorites()`)                    |
| 💬 리뷰 추가 버튼                   | 리뷰 텍스트 입력 후 저장 버튼 클릭 시 DB에 등록 (`ReviewDAO.addReview()`)                  |
| 📄 리뷰 목록                      | 현재 영화에 등록된 리뷰 리스트 출력 (`ReviewDAO.getReviewsByMovie()`)                   |
| ❤️ 즐겨찾기 목록                    | 사용자 즐겨찾기 목록 불러오기 및 삭제 기능 제공                                              |

---

## 🔗 협업 시 주고받는 정보 및 연결 흐름

| 항목                               | 주체 → 수신자            | 설명                                  |
| -------------------------------- | ------------------- | ----------------------------------- |
| DTO 객체 (`MovieDTO`, `ReviewDTO`) | API 담당 → GUI, DB 담당 | TMDB API 응답 결과를 JSON → DTO로 변환 후 전달 |
| DAO 호출용 입력 데이터                   | GUI → DB 담당         | 리뷰 추가, 즐겨찾기 등록 등에 필요한 정보 전달         |
| `.properties` 예시 파일              | 전체 팀 공유             | 개인 환경에 맞게 복사 및 설정                   |
| API 응답 구조 및 파싱 로직                | API 담당 → 팀원 공유      | JSON 구조 예시와 응답 샘플 제공                |
| 리뷰 및 즐겨찾기 조회 데이터                 | DB 담당 → GUI         | DB에서 불러온 리스트를 GUI에 출력               |

---

## ☁️ 클라우드 DB 전환 계획

현재는 로컬 MySQL DB를 기반으로 개발 중이며, 추후 **AWS RDS** 기반의 클라우드 DB 환경으로 전환할 예정입니다.

* 클라우드 전환 시 `db.properties` 내 접속 정보만 수정하면 시스템 구조 변경 없이 동작
* 보안(IP 제한), 백업, 접근 제어 등을 포함한 운영 환경을 적용할 예정

---

## 📌 주의사항

* `apikey.properties`와 `db.properties`는 Git에 업로드하지 않음 → `.gitignore`에 반드시 포함
* 예시 파일(`apikey.properties.example`, `db.properties.example`)만 GitHub에 공유
* JDBC 드라이버가 classpath에 포함되어야 정상 동작
* TMDB API는 하루 호출 수 제한이 있음

---

본 프로젝트는 영화 정보 검색과 저장을 학습 목적으로 구현한 예제이며, 상업적 용도는 아닙니다.
