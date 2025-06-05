package movie_info_system.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDTO {
    private int movieId;
    private String userId;
}
