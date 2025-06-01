package movie_info_system.dto;

public class FavoriteDTO {
    private int movieId;
    private String userId;

    public FavoriteDTO(int movieId, String userId) {
        this.movieId = movieId;
        this.userId = userId;
    }

    public int getMovieId() { return movieId; }
    public String getUserId() { return userId; }
}
