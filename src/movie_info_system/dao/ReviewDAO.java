package movie_info_system.dao;

import movie_info_system.dto.ReviewDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // 리뷰 1개 삽입
    public void insertReview(ReviewDTO review) {
        String sql = "INSERT INTO review (movie_id, reviewer, content) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getMovieId());
            pstmt.setString(2, review.getReviewer());
            pstmt.setString(3, review.getContent());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ [insertReview] 리뷰 삽입 중 오류 (movieId: " + review.getMovieId() + ", reviewer: " + review.getReviewer() + ")");
            e.printStackTrace();
        }
    }

    // 특정 영화의 리뷰 목록 조회
    public List<ReviewDTO> getReviewsByMovieId(int movieId) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE movie_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO(
                    rs.getInt("review_id"),
                    rs.getInt("movie_id"),
                    rs.getString("reviewer"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at")
                );
                list.add(review);
            }

        } catch (SQLException e) {
            System.err.println("❌ [getReviewsByMovieId] 리뷰 조회 중 오류 (movieId: " + movieId + ")");
            e.printStackTrace();
        }
        return list;
    }

    // 중복 리뷰 존재 여부 확인
    public boolean existsReview(int movieId, String reviewer) {
        String sql = "SELECT 1 FROM review WHERE movie_id = ? AND reviewer = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            pstmt.setString(2, reviewer);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // 존재하면 true

        } catch (SQLException e) {
            System.err.println("❌ [existsReview] 중복 리뷰 체크 오류 (movieId: " + movieId + ", reviewer: " + reviewer + ")");
            e.printStackTrace();
            return false;
        }
    }
}
