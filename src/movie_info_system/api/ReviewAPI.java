package movie_info_system.api;

import movie_info_system.dto.ReviewDTO; // Lombok으로 생성된 DTO
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewAPI {

    private final HttpClient httpClient;
    private final String apiKey;
    private final String baseUrl;

    public ReviewAPI() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.apiKey = TMDBConfig.getApiKey();
        this.baseUrl = TMDBConfig.getApiBaseUrl();
    }

    private String sendRequest(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .timeout(Duration.ofSeconds(5))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("HTTP Error: " + response.statusCode() + " for URI: " + uri);
            System.err.println("Response body: " + response.body());
            return null;
        }
        return response.body();
    }
    
    private ReviewDTO parseReview(JSONObject reviewJson, int movieId) {
        if (reviewJson == null) return null;

        JSONObject authorDetails = reviewJson.optJSONObject("author_details");
        double rating = -1;
        if (authorDetails != null && !authorDetails.isNull("rating")) {
            rating = authorDetails.optDouble("rating", -1);
        }

        // ✅ createdAt을 Timestamp로 변환
        Timestamp createdAtTimestamp = null;
        if (reviewJson.has("created_at") && !reviewJson.isNull("created_at")) {
            ZonedDateTime zdtCreated = ZonedDateTime.parse(reviewJson.optString("created_at"));
            createdAtTimestamp = Timestamp.valueOf(zdtCreated.toLocalDateTime());
        }

        String updatedAtStr = "";
        if (reviewJson.has("updated_at") && !reviewJson.isNull("updated_at")) {
            ZonedDateTime zdtUpdated = ZonedDateTime.parse(reviewJson.optString("updated_at"));
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            updatedAtStr = zdtUpdated.format(outputFormatter);
        }

        return ReviewDTO.builder()
                .reviewId(0)
                .movieId(movieId)
                .reviewer(reviewJson.optString("author"))
                .rating(rating)
                .content(reviewJson.optString("content"))
                .createdAt(createdAtTimestamp) // ✅ Timestamp 객체로 수정
                .updatedAt(updatedAtStr)
                .build();
    }



    /**
     * TMDB에서 특정 영화의 리뷰 목록을 가져옵니다.
     * @param movieId 영화 ID
     * @param page 페이지 번호 (1부터 시작)
     * @return 영화 리뷰 DTO 리스트
     */
    public List<ReviewDTO> getMovieReviews(int movieId, int page) {
        List<ReviewDTO> reviews = new ArrayList<>();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("API Key is not available. Cannot get movie reviews.");
            return reviews;
        }

        try {
            String urlString = String.format("%s/movie/%d/reviews?api_key=%s&language=en-US&page=%d", // 리뷰는 보통 영어로 많음
                    baseUrl, movieId, apiKey, page);
            String jsonResponse = sendRequest(URI.create(urlString));

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                return reviews;
            }

            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = responseObject.optJSONArray("results");
            if (resultsArray == null) return reviews;

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject reviewJson = resultsArray.getJSONObject(i);
                reviews.add(parseReview(reviewJson, movieId));
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching movie reviews for ID " + movieId + ": " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (JSONException e) {
            System.err.println("Error parsing movie reviews JSON for ID " + movieId + ": " + e.getMessage());
        }
        return reviews;
    }
}