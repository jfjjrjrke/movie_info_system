package movie_info_system.dao;

import movie_info_system.dto.MovieDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
	
    // ✅ 해당 movie_id가 DB에 존재하는지 확인
    public boolean existsById(int movieId) {
        String sql = "SELECT 1 FROM movie WHERE movie_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 중복 확인 후 영화 삽입
    public void insertMovie(MovieDTO movie) {
        if (existsById(movie.getMovieId())) {
            System.out.println("⚠️ 중복 영화 → 삽입 생략");
            return;
        }

        String sql = "INSERT INTO movie (movie_id, title, overview, release_date, poster_path, rating) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movie.getMovieId());
            pstmt.setString(2, movie.getTitle());
            pstmt.setString(3, movie.getOverview());
            pstmt.setDate(4, Date.valueOf(movie.getReleaseDate())); // LocalDate → SQL Date
            pstmt.setString(5, movie.getPosterPath());
            pstmt.setFloat(6, movie.getRating());

            pstmt.executeUpdate();
            System.out.println("✅ 영화 삽입 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ 전체 영화 목록 최신순으로 조회
    public List<MovieDTO> getAllMovies() {
        List<MovieDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM movie ORDER BY release_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                MovieDTO movie = new MovieDTO(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("overview"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getString("poster_path"),
                    rs.getFloat("rating")
                );
                list.add(movie);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ 특정 영화 상세 조회
    public MovieDTO getMovieById(int movieId) {
        String sql = "SELECT * FROM movie WHERE movie_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new MovieDTO(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("overview"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getString("poster_path"),
                    rs.getFloat("rating")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ 제목 키워드로 검색
    public List<MovieDTO> searchMovies(String keyword) {
        List<MovieDTO> result = new ArrayList<>();
        String sql = "SELECT * FROM movie WHERE title LIKE ? ORDER BY release_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result.add(new MovieDTO(
                    rs.getInt("movie_id"),
                    rs.getString("title"),
                    rs.getString("overview"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getString("poster_path"),
                    rs.getFloat("rating")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
    
    public Integer getMovieIdByTitle(String title) {
        String sql = "SELECT movie_id FROM movie WHERE title = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 조회 실패 시
    }
}
