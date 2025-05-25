package movie_info_system.dao;

import movie_info_system.dto.ReviewDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
	 // 리뷰 1개 삽입
    public void insertReview(ReviewDTO review) {
        String sql = "INSERT INTO reviews (movie_id, author, content) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getMovieId());
            pstmt.setString(2, review.getAuthor());
            pstmt.setString(3, review.getContent());
            pstmt.executeUpdate();

        } catch (SQLException e) {
        	System.err.println("❌ 리뷰 삽입 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 특정 영화에 대한 리뷰 목록 조회
    public List<ReviewDTO> getReviewsByMovieId(int movieId) {
        List<ReviewDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE movie_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ReviewDTO review = new ReviewDTO(
                    rs.getInt("id"),
                    rs.getInt("movie_id"),
                    rs.getString("author"),
                    rs.getString("content")
                );
                list.add(review);
            }

        } catch (SQLException e) {
        	System.err.println("❌ 리뷰 조회 중 오류 발생 (movieId: " + movieId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
