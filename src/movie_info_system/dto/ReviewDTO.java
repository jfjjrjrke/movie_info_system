package movie_info_system.dto;

import lombok.*;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private int reviewId;
    private int movieId;
    private String reviewer; // 또는 userId (DB에 맞게 유지)
    private String content;
    private Timestamp createdAt;

    // TMDB용 확장 필드
    private String updatedAt;  // ISO 포맷 시간 문자열
    private double rating;     // TMDB 사용자 평점 (0~10)
}
