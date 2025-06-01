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
├── movie_db_dump.sql          # DB 전체 보관용 덕프
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

1. `db.properties.example` 파일을 복사하여 `db.properties`로 변경 (Git에는 업로드 하지 않음)
2. 로컬 또는 클라우드 DB 접속 정보 입력

```properties
db.url=jdbc:mysql://localhost:3306/movie_db
db.user=root
db.password=your_password
```

3. `create_tables.sql` 또는 `movie_db_dump.sql`을 DBeaver에서 실행해서 테이블 구조 생성 또는 전체 보관
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

## 👥 팀원별 역할

| 이름  | 역할                                 | 주요 구현 파일                                                                                                                  |
| --- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| 김수빈 | GUI 프레임 구성 및 이벤트 제어, 리뷰/즐겨찾기 패널 구현 | `Main.java`, `MainFrame.java`, `MovieTableModel.java`, `MovieDetailPanel.java`, `ReviewPanel.java`, `FavoritesPanel.java` |
| 최우진 | API 연동 및 DTO 정의                    | `MovieAPI.java`, `ReviewAPI.java`, `TMDBConfig.java`, `MovieDTO.java`, `ReviewDTO.java`                                   |
| 지현우 | DB 연동 및 DAO 구현                     | `MovieDAO.java`, `ReviewDAO.java`, `FavoriteDAO.java`, `DBUtil.java`                                                      |

## 📌 주의사항

* `apikey.properties`과 `db.properties`는 Git에 업로드하지 않음 → `.gitignore`에 다비 추가
* 예시 파일(`apikey.properties.example`, `db.properties.example`)만 GitHub에 공유
* JDBC 드라이버가 classpath에 포함되어야 정산 동작
* TMDB API는 하루 호출 수 제한이 있음

---

본 프로젝트는 영화 정보 검색과 저장을 학습 목적으로 구현한 예제이며, 상업적 용도는 아니입니다.
