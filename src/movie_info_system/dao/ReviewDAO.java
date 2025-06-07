package movie_info_system.dao;

import movie_info_system.dto.ReviewDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    static {
        System.out.println("✅ ReviewDAO 클래스 로딩됨");
    }

    // ✅ 리뷰 삽입 (내가 작성한 리뷰 + TMDB API 공통 사용)
    public void insertReview(ReviewDTO review) throws SQLException {
        String sql = "INSERT INTO review (movie_id, reviewer, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getMovieId());
            pstmt.setString(2, review.getReviewer());
            pstmt.setString(3, review.getContent());
            pstmt.setTimestamp(4,
                    review.getCreatedAt() != null
                            ? review.getCreatedAt()
                            : new Timestamp(System.currentTimeMillis())
            );

            pstmt.executeUpdate();
            System.out.println("✅ 리뷰 등록 완료");

        } catch (SQLException e) {
            System.err.println("⛔ 리뷰 삽입 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // ✅ 특정 영화의 최신 리뷰 N개 조회 (내 리뷰 + API 통합 출력용)
    public List<ReviewDTO> getLatestReviewsByMovieId(int movieId, int limit) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE movie_id = ? ORDER BY created_at DESC LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            pstmt.setInt(2, limit);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(ReviewDTO.builder()
                        .reviewId(rs.getInt("review_id"))
                        .movieId(rs.getInt("movie_id"))
                        .reviewer(rs.getString("reviewer"))
                        .content(rs.getString("content"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .build());
            }

        } catch (SQLException e) {
            System.err.println("⛔ 리뷰 조회 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
}
