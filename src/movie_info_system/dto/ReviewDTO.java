package movie_info_system.dto;

import java.sql.Timestamp;

public class ReviewDTO {
    private int reviewId;
    private int movieId;
    private String reviewer;
    private String content;
    private Timestamp createdAt;

    // 저장용 생성자
    public ReviewDTO(int movieId, String reviewer, String content) {
        this.movieId = movieId;
        this.reviewer = reviewer;
        this.content = content;
    }

    // 조회용 생성자
    public ReviewDTO(int reviewId, int movieId, String reviewer, String content, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.reviewer = reviewer;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getReviewId() { return reviewId; }
    public int getMovieId() { return movieId; }
    public String getReviewer() { return reviewer; }
    public String getContent() { return content; }
    public Timestamp getCreatedAt() { return createdAt; }
}
