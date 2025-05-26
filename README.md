# 🎬 영화 정보 관리 프로그램

## 📌 프로젝트 소개

사용자가 원하는 영화의 정보를 TMDB API로 검색하고, 해당 영화에 대한 상세정보, 리뷰를 확인할 수 있으며, 즐겨찾기로 저장할 수 있는 Java 기반의 영화 정보 관리 시스템입니다.

## 🛠 사용 기술

* Java 8+
* Swing (GUI)
* MySQL (DB)
* JDBC (DB 연동)
* TMDB API

## 📂 프로젝트 구조

```
MovieInfoProject/
├── README.md
├── .gitignore
├── movie_db_dump.sql
├── create_tables.sql
├── db.properties
├── db.properties.example
├── apikey.properties
├── apikey.properties.example
├── src/
│   ├── main/
│   │   └── Main.java
│   ├── gui/
│   │   ├── MainFrame.java
│   │   ├── MovieTableModel.java
│   │   ├── MovieDetailPanel.java
│   │   ├── ReviewPanel.java
│   │   └── FavoritesPanel.java
│   ├── api/
│   │   ├── MovieAPI.java
│   │   ├── ReviewAPI.java
│   │   └── TMDBConfig.java
│   ├── dto/
│   │   ├── MovieDTO.java
│   │   └── ReviewDTO.java
│   ├── dao/
│   │   ├── MovieDAO.java
│   │   ├── ReviewDAO.java
│   │   └── DBUtil.java
│   └── test/
│       └── DBTest.java
```

## ▶ 실행 방법

### ✅ API 설정 방법

1. `apikey.properties.example` 파일을 `apikey.properties`로 복사
2. TMDB API 키를 입력

```properties
TMDB_API_KEY=your_tmdb_api_key
```

### ✅ DB 설정 방법

1. `db.properties.example` 파일을 `db.properties`로 복사 (Git에는 업로드하지 않음)
2. 로컬 또는 클라우드 DB 접속 정보를 입력

```properties
db.url=jdbc:mysql://localhost:3306/movie_db
db.user=root
db.password=your_password
```

3. `create_tables.sql` 또는 `movie_db_dump.sql`을 DBeaver 등에서 실행하여 DB 생성 또는 복원
4. Eclipse 또는 IntelliJ에서 `Main.java` 실행 → GUI 실행됨

### ✅ DB 연결 테스트 방법

`src/test/DBTest.java` 실행:

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

| 이름  | 역할                              | 주요 구현 파일 및 기능                                                                                                                                                                                                                                   |
| --- | ------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| 김수빈 | **GUI 프레임 구성 및 사용자 인터페이스 구현**   | - `MainFrame.java`: 전체 레이아웃 구성, 검색창 / 리뷰 패널 / 즐겨찾기 패널 포함<br>- `MovieTableModel.java`: 영화 검색 결과 테이블 구성<br>- `MovieDetailPanel.java`: 영화 상세정보 출력<br>- `ReviewPanel.java`: 리뷰 목록과 리뷰 작성 UI 구현<br>- `FavoritesPanel.java`: 즐겨찾기 영화 목록 출력 및 삭제 기능 구현 |
| 최우진 | **TMDB API 연동, DTO 정의 및 응답 처리** | - `MovieAPI.java`: TMDB 영화 검색 API 호출, 결과 JSON 파싱 후 DTO 반환<br>- `ReviewAPI.java`: TMDB 리뷰 API 호출<br>- `TMDBConfig.java`: API URL 및 키 관리<br>- `MovieDTO.java`, `ReviewDTO.java`: JSON 데이터 파싱 후 Java 객체로 변환                                        |
| 지현우 | **DB 연동 및 DAO 처리, 클라우드 DB 운영**  | - `MovieDAO.java`: 영화 정보 DB 저장 및 즐겨찾기 처리<br>- `ReviewDAO.java`: 리뷰 CRUD 기능 구현<br>- `DBUtil.java`: JDBC 기반 DB 연결 처리<br>- AWS RDS로 클라우드 DB 전환 계획: IP 접근 제어, 백업 설정 등 관리 포함                                                                         |

## 🧩 GUI 구성 요소별 기능 설명 (김수빈 담당)

| UI 구성 요소                          | 기능                                                                       |
| --------------------------------- | ------------------------------------------------------------------------ |
| 🔍 **검색창** (JTextField + JButton) | 키워드 입력 후 버튼 클릭 시 `MovieAPI.searchMovies()` 호출 → `MovieTableModel`에 결과 표시 |
| 🎬 **영화 상세정보 패널**                 | 테이블에서 영화 선택 시 상세정보 조회 후 출력                                               |
| ⭐ **즐겨찾기 버튼**                     | 선택된 영화 정보를 즐겨찾기로 DB에 저장 (`MovieDAO.addToFavorites()`)                    |
| 💬 **리뷰 추가 버튼**                   | 리뷰 텍스트 입력 후 저장 버튼 클릭 시 DB에 등록 (`ReviewDAO.addReview()`)                  |
| 📄 **리뷰 목록**                      | 현재 영화에 등록된 리뷰 리스트 출력 (`ReviewDAO.getReviewsByMovie()`)                   |
| ❤️ **즐겨찾기 목록**                    | 사용자 즐겨찾기 목록 불러오기 및 삭제 기능 제공                                              |

## 🔄 협업 시 주고받는 정보 및 연결 흐름

| 항목                               | 주체 → 수신자            | 설명                                     |
| -------------------------------- | ------------------- | -------------------------------------- |
| DTO 객체 (`MovieDTO`, `ReviewDTO`) | API 담당 → GUI, DB 담당 | TMDB API 호출 결과를 JSON → DTO 객체로 변환 후 전달 |
| DAO 호출용 입력 데이터                   | GUI → DB 담당         | 리뷰 추가, 즐겨찾기 등록 등에 필요한 정보 전달            |
| `.properties` 예시 파일              | 전체 팀 공유             | 개인 개발 환경에 맞게 복사 및 설정                   |
| API 응답 구조 및 파싱 로직                | API 담당 → 팀원 공유      | JSON 구조 문서화 또는 샘플 응답 제공                |
| 리뷰 및 즐겨찾기 조회 데이터                 | DB 담당 → GUI         | DB에서 불러온 리스트 데이터를 GUI에 출력              |

## ☁️ 클라우드 DB 전환 계획

현재는 로컬 MySQL DB를 기반으로 개발 중이며, 추후 AWS RDS 기반의 클라우드 DB 환경으로 전환할 계획입니다.

* 클라우드 전환 시 `db.properties` 내 접속 정보만 수정하면 전체 시스템 구조 변경 없이 동작
* 보안(IP 제한), 백업, 접근 제어 등을 포함한 운영 환경을 적용할 예정입니다

## 📌 주의사항

* `apikey.properties`, `db.properties`는 Git에 절대 업로드하지 않도록 `.gitignore`에 반드시 포함
* `*.example` 예시 파일만 GitHub에 업로드해 팀원은 복사하여 사용
* TMDB API는 호출 횟수 제한이 존재하므로 과도한 호출 주의
* JDBC 드라이버가 프로젝트 classpath에 포함되어야 정상 작동

---

본 프로젝트는 영화 정보 검색과 저장 기능을 학습 목적으로 구현한 예제이며, 상업적 목적의 사용은 지양합니다.