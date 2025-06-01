package movie_info_system.dao;

import movie_info_system.dto.FavoriteDTO;
import movie_info_system.dto.MovieDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    // ✅ 즐겨찾기 추가
    public boolean addFavorite(FavoriteDTO favorite) {
        String sql = "INSERT INTO favorite (movie_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, favorite.getMovieId());
            pstmt.setString(2, favorite.getUserId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 즐겨찾기 삭제
    public boolean removeFavorite(int movieId, String userId) {
        String sql = "DELETE FROM favorite WHERE movie_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 즐겨찾기 여부 확인
    public boolean isFavorite(int movieId, String userId) {
        String sql = "SELECT 1 FROM favorite WHERE movie_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            pstmt.setString(2, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 즐겨찾기한 movie_id 목록 조회
    public List<Integer> getFavoriteMovieIds(String userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT movie_id FROM favorite WHERE user_id = ? ORDER BY added_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("movie_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

    // ✅ 즐겨찾기한 영화 상세정보 조회
    public List<MovieDTO> getFavoriteMovies(String userId) {
        MovieDAO movieDAO = new MovieDAO();
        List<MovieDTO> result = new ArrayList<>();
        for (int id : getFavoriteMovieIds(userId)) {
            MovieDTO movie = movieDAO.getMovieById(id);
            if (movie != null) result.add(movie);
        }
        return result;
    }
}

