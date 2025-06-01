package movie_info_system.dto;

import java.time.LocalDate;

public class MovieDTO {
    private int movieId;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private String posterPath;
    private float rating;

    public MovieDTO(int movieId, String title, String overview, LocalDate releaseDate, String posterPath, float rating) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.rating = rating;
    }

    // getter만 넣어도 충분
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getOverview() { return overview; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public String getPosterPath() { return posterPath; }
    public float getRating() { return rating; }
}
