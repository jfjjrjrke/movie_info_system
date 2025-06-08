package movie_info_system.api;

import movie_info_system.dto.MovieDTO; // Lombok으로 생성된 DTO
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieAPI {

    private final HttpClient httpClient;
    private final String apiKey;
    private final String baseUrl;

    public MovieAPI() {
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
            System.err.println("Response body: " + response.body()); // 에러 원인 파악에 도움
            return null; // 또는 예외 발생
        }
        return response.body();
    }

    private List<MovieDTO> parseMovieList(String jsonResponse) {
        List<MovieDTO> movies = new ArrayList<>();
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            return movies;
        }
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = responseObject.optJSONArray("results");
            if (resultsArray == null) return movies;

            for (int i = 0; i < resultsArray.length(); i++) {
                movies.add(parseMovie(resultsArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            System.err.println("Error parsing movie list JSON: " + e.getMessage());
        }
        return movies;
    }

    private MovieDTO parseMovie(JSONObject movieJson) {
        if (movieJson == null) return null;

        // 장르 ID 목록을 가져와서 문자열 리스트로 변환 (실제 앱에서는 장르명 매핑 필요)
        // TMDB API는 영화 목록에서 genre_ids를, 상세 정보에서 genres 객체 배열을 제공합니다.
        // 여기서는 MovieDTO에 List<String> genres 필드가 있다고 가정하고 ID만 넣거나,
        // 또는 장르명을 가져오기 위한 추가 로직이 필요합니다.
        // 간단히 ID 리스트를 문자열로 변환하여 저장하거나, 장르명 매핑은 추후 과제로 남길 수 있습니다.
        List<String> genreNames = new ArrayList<>(); // 우선 빈 리스트로 초기화
        JSONArray genreIdsArray = movieJson.optJSONArray("genre_ids"); // 목록 응답에서 사용
        if (genreIdsArray != null) {
            for (int j = 0; j < genreIdsArray.length(); j++) {
                genreNames.add(String.valueOf(genreIdsArray.optInt(j))); // ID를 문자열로 추가
            }
        }
        // 상세 정보 응답의 경우 (parseMovieDetails 에서 활용)
        JSONArray genresArray = movieJson.optJSONArray("genres");
        if (genresArray != null) {
            genreNames.clear(); // 상세 정보의 장르명으로 대체
            for (int j = 0; j < genresArray.length(); j++) {
                JSONObject genreObject = genresArray.optJSONObject(j);
                if (genreObject != null) {
                    genreNames.add(genreObject.optString("name"));
                }
            }
        }
        
        // ✅ release_date → LocalDate로 변환
        String releaseStr = movieJson.optString("release_date");
        LocalDate releaseDate = null;
        if (releaseStr != null && !releaseStr.isEmpty()) {
            releaseDate = LocalDate.parse(releaseStr);
        }


        return MovieDTO.builder()
                .movieId(movieJson.optInt("id"))
                .title(movieJson.optString("title"))
                .originalTitle(movieJson.optString("original_title"))
                .overview(movieJson.optString("overview"))
                .releaseDate(releaseDate)
                .posterPath(movieJson.optString("poster_path"))
                .backdropPath(movieJson.optString("backdrop_path"))
                .voteAverage(movieJson.optDouble("vote_average"))
                .voteCount(movieJson.optInt("vote_count"))
                .popularity(movieJson.optDouble("popularity"))
                .genres(genreNames) // 장르명 리스트 또는 ID 리스트
                .rating((float) movieJson.optDouble("vote_average", 0.0))  // 평점
                .build();
    }

    /**
     * TMDB에서 영화를 검색합니다.
     * @param query 검색어
     * @param page 페이지 번호 (1부터 시작)
     * @return 검색된 영화 DTO 리스트
     */
    public List<MovieDTO> searchMovies(String query, int page, String language) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("API Key is not available. Cannot search movies.");
            return new ArrayList<>();
        }
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlString = String.format("%s/search/movie?api_key=%s&language=%s&query=%s&page=%d&include_adult=false",
                    baseUrl, apiKey, language, encodedQuery, page);
            String jsonResponse = sendRequest(URI.create(urlString));
            return parseMovieList(jsonResponse);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error searching movies: " + e.getMessage());
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }
    }

    // 다국어(한국어 + 영어) 검색 결과 합치기 (중복 제거)
    public List<MovieDTO> searchMoviesMultiLang(String query, int page) {
        List<MovieDTO> koreanResults = searchMovies(query, page, "ko-KR");
        List<MovieDTO> englishResults = searchMovies(query, page, "en-US");

        // Map에 영화 ID를 키로 하여 중복 제거
        Map<Integer, MovieDTO> uniqueMovies = new LinkedHashMap<>();
        for (MovieDTO m : koreanResults) {
            uniqueMovies.put(m.getMovieId(), m);
        }
        for (MovieDTO m : englishResults) {
            uniqueMovies.putIfAbsent(m.getMovieId(), m);
        }
        return new ArrayList<>(uniqueMovies.values());
    }

    /**
     * TMDB에서 인기 영화 목록을 가져옵니다.
     * @param page 페이지 번호 (1부터 시작)
     * @return 인기 영화 DTO 리스트
     */
    public List<MovieDTO> getPopularMovies(int page) {
         if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("API Key is not available. Cannot get popular movies.");
            return new ArrayList<>();
        }
        try {
            String urlString = String.format("%s/movie/popular?api_key=%s&language=ko-KR&page=%d",
                    baseUrl, apiKey, page);
            String jsonResponse = sendRequest(URI.create(urlString));
            return parseMovieList(jsonResponse);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching popular movies: " + e.getMessage());
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }
    }

    /**
     * TMDB에서 특정 영화의 상세 정보를 가져옵니다.
     * @param movieId 영화 ID
     * @return 영화 상세 정보 DTO
     */
    public MovieDTO getMovieDetails(int movieId) {
         if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("API Key is not available. Cannot get movie details.");
            return null;
        }
        try {
            // append_to_response를 사용하여 credits(출연진) 정보도 함께 요청 가능
            String urlString = String.format("%s/movie/%d?api_key=%s&language=ko-KR", // &append_to_response=credits
                    baseUrl, movieId, apiKey);
            String jsonResponse = sendRequest(URI.create(urlString));
            if (jsonResponse == null || jsonResponse.isEmpty()) return null;

            return parseMovie(new JSONObject(jsonResponse));
        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching movie details for ID " + movieId + ": " + e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        } catch (JSONException e) {
            System.err.println("Error parsing movie details JSON for ID " + movieId + ": " + e.getMessage());
            return null;
        }
    }
}