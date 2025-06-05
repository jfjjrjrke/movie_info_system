package movie_info_system.gui;

import javax.swing.*;

import movie_info_system.dao.MovieDAO;
import movie_info_system.dao.ReviewDAO;
import movie_info_system.dto.ReviewDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ReviewPanel extends JPanel {
    private JTextArea inputArea;
    private JButton saveButton;
    private DefaultListModel<String> reviewListModel;
    private JList<String> reviewList;
    private String currentMovieTitle = null;

    /**
     * 생성자 - 리뷰 입력창, 저장 버튼, 리뷰 리스트 구성
     */
    public ReviewPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), "리뷰"));
        setAlignmentX(Component.LEFT_ALIGNMENT);  // ReviewPanel 자체 정렬

        // 입력 영역 설정
        inputArea = new JTextArea(3, 40);
        inputArea.setMargin(new Insets(5, 5, 5, 5)); // 내부 여백 최소화
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(inputScroll);

        add(Box.createVerticalStrut(5));  // 여백

        // 저장 버튼 설정
        saveButton = new JButton("리뷰 저장");
        saveButton.setMaximumSize(new Dimension(100, 30));
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(saveButton);

        add(Box.createVerticalStrut(10)); // 여백

        // 리뷰 목록 설정
        reviewListModel = new DefaultListModel<>();
        reviewList = new JList<>(reviewListModel);
        JScrollPane listScroll = new JScrollPane(reviewList);
        listScroll.setPreferredSize(new Dimension(100, 100));
        listScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(listScroll);

        // 저장 버튼 클릭 시 동작 (중복 검사 없이 무조건 저장)
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMovieTitle != null && !inputArea.getText().trim().isEmpty()) {
                    String reviewContent = inputArea.getText().trim();
                    inputArea.setText("");

                    MovieDAO movieDAO = new MovieDAO();
                    Integer movieId = movieDAO.getMovieIdByTitle(currentMovieTitle);
                    if (movieId == null) return;

                    ReviewDAO reviewDAO = new ReviewDAO();

                    ReviewDTO dto = ReviewDTO.builder()
                            .movieId(movieId)
                            .reviewer("guest")
                            .content(reviewContent)
                            .build();

                    // 중복 검사 없이 무조건 저장
                    try {
						reviewDAO.insertReview(dto);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

                    // 리뷰 목록 새로고침
                    List<ReviewDTO> reviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);
                    reviewListModel.clear();
                    for (ReviewDTO r : reviews) {
                        reviewListModel.addElement(r.getReviewer() + ": " + r.getContent());
                    }
                }
            }
        });
    }

    /**
     * 현재 보여줄 영화 제목 설정 및 DB 리뷰 불러오기
     * @param title 영화 제목
     */
    public void setMovie(String title) {
        this.currentMovieTitle = title;

        MovieDAO movieDAO = new MovieDAO();
        Integer movieId = movieDAO.getMovieIdByTitle(title);

        if (movieId == null) {
            reviewListModel.clear();
            return;
        }

        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewDTO> reviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);

        reviewListModel.clear();
        for (ReviewDTO review : reviews) {
            reviewListModel.addElement(review.getReviewer() + ": " + review.getContent());
        }
    }
}
