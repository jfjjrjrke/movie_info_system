package movie_info_system.dao;

import movie_info_system.dto.MovieDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public void insertMovie(MovieDTO movie) {
        String sql = "INSERT INTO movies (movie_id, title, overview, release_date, poster_path, rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movie.getMovieId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getOverview());
            pstmt.setString(4, movie.getReleaseDate());
            pstmt.setString(5, movie.getPosterPath());
            pstmt.setFloat(6, movie.getRating());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ 영화 저장 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<MovieDTO> getAllMovies() {
        List<MovieDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MovieDTO movie = new MovieDTO(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("overview"),
                    rs.getString("release_date"),
                    rs.getString("poster_path"),
                    rs.getFloat("rating")
                );
                list.add(movie);
            }

        } catch (SQLException e) {
            System.err.println("❌ 영화 목록 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsById(int movieId) {
        String sql = "SELECT 1 FROM movies WHERE movie_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();  // 존재하면 true

        } catch (SQLException e) {
            System.err.println("❌ [existsById] 영화 중복 체크 실패 (movie_id: " + movieId + ")");
            e.printStackTrace();
            return false;
        }
    }
}
