package movie_info_system.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
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

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("영화 검색 프로그램");
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 상단 검색 패널 구성
        JPanel searchPanel = new JPanel(new BorderLayout());

        JPanel leftSearchPanel = new JPanel();
        searchField = new JTextField(25);
        JButton searchButton = new JButton("검색");
        leftSearchPanel.add(searchField);
        leftSearchPanel.add(searchButton);

        // 엔터키 입력시 검색 수행
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

        // 중앙 패널: 영화 목록 테이블 + 리뷰 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "제목", "개봉일", "평점" });
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        tableScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300)); // 높이 고정
        tableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(tableScrollPane);

        // 리뷰 패널 생성 및 기본 숨김 처리
        reviewPanel = new ReviewPanel();
        reviewPanel.setPreferredSize(new Dimension(600, 200));
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        reviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewPanel.setVisible(false);
        centerPanel.add(reviewPanel);

        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        // 우측 패널: 영화 상세정보 + 즐겨찾기 목록
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);

        detailPanel = new MovieDetailPanel();
        rightPanel.add(detailPanel);

        favoritesPanel = new FavoritesPanel(USER_ID);
        rightPanel.add(favoritesPanel);
        favoritesPanel.loadFavoritesFromDB();

        // 즐겨찾기 추가 버튼 이벤트 처리
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

        // 영화 목록 클릭 시 상세정보 및 리뷰 표시
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
                        // 리뷰 API 테스트 코드 추가
                        ReviewAPI reviewAPI = new ReviewAPI();
                        List<ReviewDTO> reviews = reviewAPI.getMovieReviews(movieId, 1);

                        System.out.println("API로부터 가져온 리뷰 개수: " + reviews.size());
                        for (ReviewDTO r : reviews) {
                            System.out.println(r.getReviewer() + ": " + r.getContent());
                        }

                        // 기존 리뷰 패널 업데이트 부분 유지
                        reviewPanel.setMovie(title);
                        reviewPanel.setVisible(true);
                    }

                    reviewPanel.setMovie(title);  // 리뷰 패널 데이터 업데이트
                    reviewPanel.setVisible(true);
                }
            }
        });

        // 즐겨찾기 목록 더블 클릭 시 영화 상세정보 + 리뷰 표시, 테이블 해당 영화만 표시
        favoritesPanel.getFavoritesList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    String selectedTitle = favoritesPanel.getFavoritesList().getSelectedValue();
                    if(selectedTitle == null || selectedTitle.isEmpty()) return;

                    MovieDAO movieDAO = new MovieDAO();
                    Integer movieId = movieDAO.getMovieIdByTitle(selectedTitle);
                    MovieDTO movie = movieDAO.getMovieById(movieId);

                    if(movie != null) {
                        tableModel.setRowCount(0);
                        tableModel.addRow(new Object[] {
                            movie.getTitle(),
                            movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                            movie.getRating()
                        });

                        String baseImageUrl = "https://image.tmdb.org/t/p/w500";
                        String posterPath = movie.getPosterPath();
                        ImageIcon posterIcon = null;
                        if(posterPath != null && !posterPath.isEmpty()) {
                            posterIcon = loadPosterImage(baseImageUrl + posterPath);
                        }

                        detailPanel.setMovieDetail(
                                movie.getTitle(),
                                movie.getReleaseDate() != null ? movie.getReleaseDate().toString() : "",
                                String.valueOf(movie.getRating()),
                                movie.getOverview(),
                                posterIcon
                        );

                        reviewPanel.setMovie(selectedTitle);
                        reviewPanel.setVisible(true);

                        table.setRowSelectionInterval(0, 0);
                        table.scrollRectToVisible(table.getCellRect(0, 0, true));
                    }
                }
            }
        });

        // 프로그램 시작 시 인기 영화 자동 저장 및 화면 표시
        fetchAndStorePopularMovies(1);
        showPopularMoviesFromDB();
    }

    /**
     * JFrame 반환 메서드 (외부 접근용)
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * 영화 검색 메서드
     * 검색어 없으면 DB 전체 목록, 있으면 DB 검색 후 없으면 API 검색 + DB 저장 후 다시 검색
     */
    private void performSearch() {
        String query = searchField.getText().trim();

        MovieDAO movieDAO = new MovieDAO();
        ReviewAPI reviewAPI = new ReviewAPI();
        ReviewDAO reviewDAO = new ReviewDAO();
        MovieAPI movieAPI = new MovieAPI();

        List<MovieDTO> movies;

        if (query.isEmpty()) {
            movies = movieDAO.getAllMovies();
        } else {
            // 1. DB에서 영화 검색
            movies = movieDAO.searchMovies(query);
            System.out.println("🔎 기존 DB 검색 결과: " + movies.size() + "건");

            // 2. TMDB API에서 항상 검색 시도
            List<MovieDTO> apiMovies = movieAPI.searchMoviesMultiLang(query, 1);
            System.out.println("🌐 TMDB API 검색 결과: " + apiMovies.size() + "건");

            for (MovieDTO m : apiMovies) {
                System.out.println("🎬 처리 중: " + m.getTitle() + " (ID: " + m.getMovieId() + ")");

                if (!movieDAO.existsById(m.getMovieId())) {
                    movieDAO.insertMovie(m);
                }

                if (movieDAO.existsById(m.getMovieId())) {
                    List<ReviewDTO> apiReviews = reviewAPI.getMovieReviews(m.getMovieId(), 1);
                    System.out.println("📥 리뷰 가져옴: " + apiReviews.size() + "개");

                    for (ReviewDTO r : apiReviews) {
                        System.out.println("🔥 리뷰 삽입 시도 → reviewer=" + r.getReviewer());

                        try {
                            reviewDAO.insertReview(r);  // 중복 검사 없이 무조건 삽입
                            System.out.println("✅ 리뷰 삽입 완료: " + r.getReviewer());
                        } catch (Exception e) {
                            System.err.println("⛔ 예외 발생 → " + r.getReviewer());
                            e.printStackTrace();
                        }
                    }

                }

            // 3. 영화 삽입 후 다시 DB에서 검색
            movies = movieDAO.searchMovies(query);
            System.out.println("🟩 DB 최종 검색 결과: " + movies.size() + "건");
            }
        }

        updateTable(movies);// 테이블 갱신
    }


    


    /**
     * JTable 영화 목록 업데이트
     */
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

    /**
     * URL에서 포스터 이미지 로드 및 리사이징
     */
    public ImageIcon loadPosterImage(String urlString) {
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            Image img = ImageIO.read(url);
            Image scaledImg = img.getScaledInstance(150, 225, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch(Exception e) {
            System.err.println("포스터 이미지 로딩 실패: " + e.getMessage());
            return null;
        }
    }

    /**
     * TMDB 인기 영화 목록 API 호출 및 DB 저장 (중복 제외)
     */
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

    /**
     * DB에서 인기 영화 전체 불러와 JTable에 표시
     */
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
