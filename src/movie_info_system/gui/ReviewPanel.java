package movie_info_system.gui;

import javax.swing.*;

import movie_info_system.dao.MovieDAO;
import movie_info_system.dao.ReviewDAO;
import movie_info_system.dto.ReviewDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ReviewPanel extends JPanel {
    private JTextArea inputArea;
    private JButton saveButton;
    private DefaultListModel<String> reviewListModel;
    private JList<String> reviewList;
    private String currentMovieTitle = null;

    public ReviewPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), "리뷰"));
        setAlignmentX(Component.LEFT_ALIGNMENT);  // ⬅ ReviewPanel 자체 정렬

        // 🔹 입력 영역
        inputArea = new JTextArea(3, 40);
        inputArea.setMargin(new Insets(5, 5, 5, 5)); // 내부 여백 최소화
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(inputScroll);

        // 🔹 여백
        add(Box.createVerticalStrut(5));

        // 🔹 저장 버튼 (크기 고정)
        saveButton = new JButton("리뷰 저장");
        saveButton.setMaximumSize(new Dimension(100, 30));  // 너비, 높이 제한
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(saveButton);

        // 🔹 여백
        add(Box.createVerticalStrut(10));

        // 🔹 리뷰 목록
        reviewListModel = new DefaultListModel<>();
        reviewList = new JList<>(reviewListModel);
        JScrollPane listScroll = new JScrollPane(reviewList);
        listScroll.setPreferredSize(new Dimension(100, 100));
        listScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(listScroll);

        // 🔹 저장 버튼 동작
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMovieTitle != null && !inputArea.getText().trim().isEmpty()) {
                    String review = inputArea.getText().trim();
                    inputArea.setText("");

                    MovieDAO movieDAO = new MovieDAO();
                    Integer movieId = movieDAO.getMovieIdByTitle(currentMovieTitle);
                    if (movieId == null) return;

                    ReviewDAO reviewDAO = new ReviewDAO();
                    ReviewDTO dto = new ReviewDTO(movieId, "guest", review);  // 작성자 고정
                    reviewDAO.insertReview(dto);

                    // 리뷰 목록 새로고침
                    List<ReviewDTO> reviews = reviewDAO.getReviewsByMovieId(movieId);
                    reviewListModel.clear();
                    for (ReviewDTO r : reviews) {
                        reviewListModel.addElement(r.getReviewer() + ": " + r.getContent());
                    }
                }
            }
        });
    }

    public void setMovie(String title) {
    	this.currentMovieTitle = title;

        // ✅ movie_id를 가져오기
        MovieDAO movieDAO = new MovieDAO();
        Integer movieId = movieDAO.getMovieIdByTitle(title);

        if (movieId == null) {
            reviewListModel.clear();
            return;
        }

        // ✅ DB에서 리뷰 리스트 불러오기
        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewDTO> reviews = reviewDAO.getReviewsByMovieId(movieId);

        // ✅ 리스트에 출력
        reviewListModel.clear();
        for (ReviewDTO review : reviews) {
            reviewListModel.addElement(review.getReviewer() + ": " + review.getContent());
        }
    }

}