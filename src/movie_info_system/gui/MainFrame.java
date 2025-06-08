package movie_info_system.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import movie_info_system.api.MovieAPI;
import movie_info_system.api.ReviewAPI;
import movie_info_system.dao.FavoriteDAO;
import movie_info_system.dao.MovieDAO;
import movie_info_system.dao.ReviewDAO;
import movie_info_system.dto.FavoriteDTO;
import movie_info_system.dto.MovieDTO;
import movie_info_system.dto.ReviewDTO;

public class MainFrame {

    private JFrame frame;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private MovieDetailPanel detailPanel;
    private FavoritesPanel favoritesPanel;
    private ReviewPanel reviewPanel;   // 리뷰 패널 추가
    private static final String USER_ID = "guest";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainFrame window = new MainFrame();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainFrame() {
        initialize();
    }

    // 기본 구성을 설정하는 메서드
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("MovieMate");
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 상단 검색 패널
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel leftSearchPanel = new JPanel();
        searchField = new JTextField(25);
        JButton searchButton = new JButton("검색");
        leftSearchPanel.add(searchField);
        leftSearchPanel.add(searchButton);

        // 엔터키 입력 시 검색 실행
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
        searchButton.addActionListener(e -> performSearch());

        JButton favButton = new JButton("즐겨찾기 추가");
        searchPanel.add(leftSearchPanel, BorderLayout.WEST);
        searchPanel.add(favButton, BorderLayout.EAST);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

        // 중앙 패널 - 영화 목록 테이블 + 리뷰 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "제목", "개발일", "평점" });
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        tableScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        tableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(tableScrollPane);

        // 리뷰 패널 생성 (기본은 숨기기)
        reviewPanel = new ReviewPanel();
        reviewPanel.setPreferredSize(new Dimension(600, 200));
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        reviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewPanel.setVisible(false);
        centerPanel.add(reviewPanel);

        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        
        
        // TMDB 로고 + 출처 문구 표시 (하단)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        try {
            // resources/assets/logo_tmdb.png → 클래스패스 기준 경로로 불러오기
            URL logoUrl = getClass().getResource("/assets/logo_tmdb.png");
            
            if (logoUrl != null) {
                Image img = ImageIO.read(logoUrl);
                Image scaled = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                JLabel logoLabel = new JLabel(new ImageIcon(scaled));
                footerPanel.add(logoLabel);
            } else {
                System.err.println("TMDB 로고 이미지 리소스를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("TMDB 로고 로드 실패: " + e.getMessage());
        }

        // TMDB 출처 문구
        JLabel attributionLabel = new JLabel(
            "<html>This product uses the TMDB API but is not endorsed or certified by TMDB.</html>"
        );
        attributionLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        attributionLabel.setForeground(Color.GRAY);
        footerPanel.add(attributionLabel);

        // 하단에 추가
        frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);
        
        
        

        // 우측 패널 - 영화 세부정보 + 즐겨찾기
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);

        detailPanel = new MovieDetailPanel();
        rightPanel.add(detailPanel);

        favoritesPanel = new FavoritesPanel(USER_ID);
        rightPanel.add(favoritesPanel);
        favoritesPanel.loadFavoritesFromDB();

        // 즐겨찾기 등록 버튼
        favButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String title = table.getValueAt(row, 0).toString();
                MovieDAO movieDAO = new MovieDAO();
                Integer movieId = movieDAO.getMovieIdByTitle(title);
                if (movieId != null) {
                    FavoriteDAO favoriteDAO = new FavoriteDAO();
                    if (!favoriteDAO.isFavorite(movieId, USER_ID)) {
                        favoriteDAO.addFavorite(new FavoriteDTO(movieId, USER_ID));
                        favoritesPanel.addFavorite(title);
                    } else {
                        JOptionPane.showMessageDialog(frame, "이미 즐겨찾기에 추가된 영화입니다.");
                    }
                }
            }
        });

        // 테이블 선택 시 상세 + 리뷰 표시
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String title = table.getValueAt(row, 0).toString();
                    MovieDAO movieDAO = new MovieDAO();
                    Integer movieId = movieDAO.getMovieIdByTitle(title);
                    MovieDTO movie = movieDAO.getMovieById(movieId);
                    if (movie != null) {
                        String baseImageUrl = "https://image.tmdb.org/t/p/w500";
                        String posterPath = movie.getPosterPath();
                        ImageIcon posterIcon = null;
                        if (posterPath != null && !posterPath.isEmpty()) {
                            posterIcon = loadPosterImage(baseImageUrl + posterPath);
                        }
                        detailPanel.setMovieDetail(
                                movie.getTitle(),
                                movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                                String.valueOf(movie.getRating()),
                                movie.getOverview(),
                                posterIcon
                        );

                        // ✅ 리뷰 패널에도 현재 영화 제목 설정 (리뷰 저장 작동하려면 꼭 필요)
                        reviewPanel.setMovie(title);

                        // TMDB + 내가 쓴 리뷰 같이 보여줌
                        ReviewAPI reviewAPI = new ReviewAPI();
                        List<ReviewDTO> apiReviews = reviewAPI.getMovieReviews(movieId, 1);
                        ReviewDAO reviewDAO = new ReviewDAO();
                        List<ReviewDTO> myReviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);
                        List<ReviewDTO> allReviews = new ArrayList<>();
                        allReviews.addAll(myReviews);
                        allReviews.addAll(apiReviews);

                        reviewPanel.setReviews(allReviews);
                        reviewPanel.setVisible(true);
                        System.out.println("💬 내가 쓴 리뷰 " + myReviews.size() + "개, TMDB 리뷰 " + apiReviews.size() + "개");
                    }
                }
            }
        });


        // 즐겨찾기에서 영화 선택 시 불러오기
        favoritesPanel.getFavoritesList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedTitle = favoritesPanel.getFavoritesList().getSelectedValue();
                    if (selectedTitle == null || selectedTitle.isEmpty()) return;

                    MovieDAO movieDAO = new MovieDAO();
                    Integer movieId = movieDAO.getMovieIdByTitle(selectedTitle);
                    MovieDTO movie = movieDAO.getMovieById(movieId);

                    if (movie != null) {
                        tableModel.setRowCount(0);
                        tableModel.addRow(new Object[]{
                            movie.getTitle(),
                            movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                            movie.getRating()
                        });

                        String baseImageUrl = "https://image.tmdb.org/t/p/w500";
                        String posterPath = movie.getPosterPath();
                        ImageIcon posterIcon = null;
                        if (posterPath != null && !posterPath.isEmpty()) {
                            posterIcon = loadPosterImage(baseImageUrl + posterPath);
                        }

                        detailPanel.setMovieDetail(
                                movie.getTitle(),
                                movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                                String.valueOf(movie.getRating()),
                                movie.getOverview(),
                                posterIcon
                        );

                        // ✅ 리뷰 패널 수동 갱신 (내 리뷰 + TMDB 리뷰)
                        ReviewDAO reviewDAO = new ReviewDAO();
                        ReviewAPI reviewAPI = new ReviewAPI();
                        List<ReviewDTO> myReviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);
                        List<ReviewDTO> apiReviews = reviewAPI.getMovieReviews(movieId, 1);
                        List<ReviewDTO> allReviews = new ArrayList<>();
                        allReviews.addAll(myReviews);
                        allReviews.addAll(apiReviews);
                        reviewPanel.setReviews(allReviews);
                        reviewPanel.setVisible(true);

                        table.setRowSelectionInterval(0, 0);
                        table.scrollRectToVisible(table.getCellRect(0, 0, true));
                    }
                }
            }
        });


        // 프로그램 시작시 인기 영화 DB에 저장 + 표시
        fetchAndStorePopularMovies(1);
        showPopularMoviesFromDB();
    }

    // JFrame 반환 (보유에 필요)
    public JFrame getFrame() {
        return frame;
    }

    // 영화 검색 시 처리 방식
    private void performSearch() {
        String query = searchField.getText().trim();
        MovieDAO movieDAO = new MovieDAO();
        ReviewAPI reviewAPI = new ReviewAPI();
        MovieAPI movieAPI = new MovieAPI();
        List<MovieDTO> movies;

        if (query.isEmpty()) {
            movies = movieDAO.getAllMovies();
        } else {
            movies = movieDAO.searchMovies(query);
            List<MovieDTO> apiMovies = movieAPI.searchMoviesMultiLang(query, 1);
            for (MovieDTO m : apiMovies) {
                if (!movieDAO.existsById(m.getMovieId())) {
                    movieDAO.insertMovie(m);
                }
            }
            movies = movieDAO.searchMovies(query);
        }

        updateTable(movies);

        if (!movies.isEmpty()) {
            MovieDTO firstMovie = movies.get(0);
            List<ReviewDTO> apiReviews = reviewAPI.getMovieReviews(firstMovie.getMovieId(), 1);
            SwingUtilities.invokeLater(() -> {
                reviewPanel.setReviews(apiReviews);
                reviewPanel.setVisible(true);
            });
        }
    }

    // JTable 목록 갱신
    private void updateTable(List<MovieDTO> movies) {
        tableModel.setRowCount(0);
        if(movies.isEmpty()) {
            tableModel.addRow(new Object[] {"검색 결과가 없습니다.", "", ""});
            return;
        }
        for(MovieDTO m : movies) {
            tableModel.addRow(new Object[] {
                    m.getTitle() != null ? m.getTitle() : "",
                    m.getReleaseDate() != null ? m.getReleaseDate().toString() : "",
                    m.getRating()
            });
        }
    }

    // URL 이미지 로드
    public ImageIcon loadPosterImage(String urlString) {
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            Image img = ImageIO.read(url);
            Image scaledImg = img.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch(Exception e) {
            System.err.println("포스터 이미지 로드 실패: " + e.getMessage());
            return null;
        }
    }

    // 인기 영화 데이터 발견 + DB 저장
    public void fetchAndStorePopularMovies(int page) {
        MovieAPI movieAPI = new MovieAPI();
        MovieDAO movieDAO = new MovieDAO();
        List<MovieDTO> popularMovies = movieAPI.getPopularMovies(page);
        for(MovieDTO movie : popularMovies) {
            if(!movieDAO.existsById(movie.getMovieId())) {
                movieDAO.insertMovie(movie);
            }
        }
    }

    // 인기 영화를 표시하기위해 JTable에 갱신
    private void showPopularMoviesFromDB() {
        MovieDAO movieDAO = new MovieDAO();
        List<MovieDTO> popularMovies = movieDAO.getAllMovies();
        tableModel.setRowCount(0);
        for(MovieDTO movie : popularMovies) {
            tableModel.addRow(new Object[] {
                    movie.getTitle(),
                    movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                    movie.getRating()
            });
        }
    }
}
