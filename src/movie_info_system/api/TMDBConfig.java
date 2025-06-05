package movie_info_system.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class TMDBConfig {

    private static final String API_KEY;
    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    public static final String IMAGE_BASE_URL_W500 = "https://image.tmdb.org/t/p/w500"; // 일반 포스터 크기
    public static final String IMAGE_BASE_URL_ORIGINAL = "https://image.tmdb.org/t/p/original"; // 원본 크기

    static {
        Properties props = new Properties();
        // 프로젝트 루트를 기준으로 apikey.properties 파일을 찾습니다.
        String propertiesPath = Paths.get("apikey.properties").toAbsolutePath().toString();
        try (InputStream input = new FileInputStream(propertiesPath)) {
            props.load(input);
            API_KEY = props.getProperty("TMDB_API_KEY");
            if (API_KEY == null || API_KEY.trim().isEmpty()) {
                System.err.println("TMDB_API_KEY is not found in apikey.properties. Please check the file.");
                // 실제 운영 환경에서는 여기서 예외를 발생시키거나, 기본 키를 사용하거나, 프로그램을 종료할 수 있습니다.
            }
        } catch (IOException ex) {
            System.err.println("Error loading apikey.properties: " + ex.getMessage());
            // API_KEY가 로드되지 않았을 경우를 대비한 기본값 또는 예외 처리
            throw new RuntimeException("Failed to load API key from apikey.properties", ex);
        }
    }

    public static String getApiKey() {
        if (API_KEY == null || API_KEY.trim().isEmpty() || "여기에_실제_TMDB_API_키를_입력하세요".equals(API_KEY)) {
             System.err.println("Warning: TMDB API Key is not configured properly. Please set it in apikey.properties.");
        }
        return API_KEY;
    }

    public static String getApiBaseUrl() {
        return API_BASE_URL;
    }
}