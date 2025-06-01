package movie_info_system.gui;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import movie_info_system.dao.FavoriteDAO;
import movie_info_system.dao.MovieDAO;
import movie_info_system.dto.FavoriteDTO;
import movie_info_system.dto.MovieDTO;

public class MainFrame {

	private JFrame frame;
	private JTextField searchField;
	private JTable table;
	private DefaultTableModel tableModel;
	private MovieDetailPanel detailPanel;
	private FavoritesPanel favoritesPanel;
	private ReviewPanel reviewPanel;
	private static final String USER_ID = "guest";
	

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
        frame = new JFrame();
        frame.setTitle("영화 검색 프로그램");
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // 🔍 상단 검색 패널 (검색창 왼쪽, 즐겨찾기 버튼 오른쪽)
        JPanel searchPanel = new JPanel(new BorderLayout());

        // 왼쪽 검색창 구성
        JPanel leftSearchPanel = new JPanel();
        searchField = new JTextField(25);
        JButton searchButton = new JButton("검색");
        leftSearchPanel.add(searchField);
        leftSearchPanel.add(searchButton);
        
        // 🔑 Enter 키로 검색 가능하게 추가
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch(); // 아래에 이 메서드 정의 필요
                }
            }
        });

        // 검색 버튼 클릭 시도 동일한 동작
        searchButton.addActionListener(e -> performSearch());

        // 오른쪽 즐겨찾기 추가 버튼
        JButton favButton = new JButton("즐겨찾기 추가");

        searchPanel.add(leftSearchPanel, BorderLayout.WEST);
        searchPanel.add(favButton, BorderLayout.EAST);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

        // 📦 중앙 영역: 테이블 + 리뷰
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 수직 정렬

        // 📋 영화 목록 테이블
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "제목", "개봉일", "평점" });
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(600, 300));
        tableScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300)); // ✅ 폭 고정
        tableScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(tableScrollPane);

        // 📝 리뷰 패널
        reviewPanel = new ReviewPanel();
        reviewPanel.setPreferredSize(new Dimension(600, 200));
        reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // ✅ 폭 고정
        reviewPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewPanel.setVisible(false);  // ✅ 처음에는 숨김
        centerPanel.add(reviewPanel);

        // 중앙 영역 프레임에 추가
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);

        // 📌 우측 패널 (상세정보 + 즐겨찾기 목록)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);

        detailPanel = new MovieDetailPanel();
        rightPanel.add(detailPanel);

        favoritesPanel = new FavoritesPanel(USER_ID);
        rightPanel.add(favoritesPanel);
        favoritesPanel.loadFavoritesFromDB();

        // 이벤트 리스너 등록
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

        searchButton.addActionListener(e -> {
        	String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                MovieDAO dao = new MovieDAO();
                List<MovieDTO> result = dao.searchMovies(query);  // ✅ DB에서 검색

                tableModel.setRowCount(0);
                for (MovieDTO m : result) {
                    tableModel.addRow(new Object[]{m.getTitle(), m.getReleaseDate(), m.getRating()});
                }
            }
        });

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
                        detailPanel.setMovieDetail(
                            movie.getTitle(),
                            movie.getReleaseDate().toString(),
                            String.valueOf(movie.getRating()),
                            movie.getOverview(),
                            null // 포스터 이미지는 현재 미연동
                        );
                    }

                    reviewPanel.setMovie(title); // 리뷰 패널도 업데이트
                    reviewPanel.setVisible(true);
                }
            }
        });
        
        // 🎯 즐겨찾기 더블 클릭 시 테이블에서 자동 선택 및 정보 표시
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
                        // ✅ 테이블에 표시 (초기화 후 추가)
                        tableModel.setRowCount(0);
                        tableModel.addRow(new Object[] {
                            movie.getTitle(),
                            movie.getReleaseDate(),
                            movie.getRating()
                        });

                        // ✅ 상세 정보 패널 업데이트
                        detailPanel.setMovieDetail(
                            movie.getTitle(),
                            movie.getReleaseDate().toString(),
                            String.valueOf(movie.getRating()),
                            movie.getOverview(),
                            null
                        );

                        // ✅ 리뷰 패널 업데이트
                        reviewPanel.setMovie(selectedTitle);
                        reviewPanel.setVisible(true);

                        // ✅ 테이블 선택
                        table.setRowSelectionInterval(0, 0);
                        table.scrollRectToVisible(table.getCellRect(0, 0, true));
                    }
                }
            }
        });
    }
	
	// MainFrame.java 내부에 아래 메서드 추가
	public JFrame getFrame() {
	    return frame;
	}

	private void performSearch() {
	    String query = searchField.getText().trim();
	    if (!query.isEmpty()) {
	        MovieDAO dao = new MovieDAO();
	        List<MovieDTO> result = dao.searchMovies(query);

	        tableModel.setRowCount(0);
	        for (MovieDTO m : result) {
	            tableModel.addRow(new Object[]{m.getTitle(), m.getReleaseDate(), m.getRating()});
	        }
	    }
	}

}