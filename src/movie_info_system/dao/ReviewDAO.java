package movie_info_system.dao;

import movie_info_system.dto.ReviewDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // ✅ 리뷰 삽입
    public void insertReview(ReviewDTO review) {
        String sql = "INSERT INTO review (movie_id, reviewer, content) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getMovieId());
            pstmt.setString(2, review.getReviewer());
            pstmt.setString(3, review.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ 영화별 리뷰 조회 (최신순)
    public List<ReviewDTO> getReviewsByMovieId(int movieId) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE movie_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new ReviewDTO(
                    rs.getInt("review_id"),
                    rs.getInt("movie_id"),
                    rs.getString("reviewer"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ 중복 리뷰 확인
    public boolean existsReview(int movieId, String reviewer) {
        String sql = "SELECT 1 FROM review WHERE movie_id = ? AND reviewer = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, reviewer);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 리뷰 삭제
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 페이징된 리뷰 조회
    public List<ReviewDTO> getReviewsByMovieIdPaged(int movieId, int page, int pageSize) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE movie_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        int offset = (page - 1) * pageSize;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setInt(2, pageSize);
            pstmt.setInt(3, offset);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new ReviewDTO(
                    rs.getInt("review_id"),
                    rs.getInt("movie_id"),
                    rs.getString("reviewer"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
