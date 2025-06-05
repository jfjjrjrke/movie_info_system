package movie_info_system.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private int movieId; // DB 저장용 기본 ID
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private String posterPath;
    private float rating;

    // TMDB용 확장 필드 (필요한 경우 활용)
    private String originalTitle;
    private String backdropPath;
    private double voteAverage;
    private int voteCount;
    private double popularity;
    private List<String> genres;
}
