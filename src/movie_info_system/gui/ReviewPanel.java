package movie_info_system.gui;

import javax.swing.*;

import movie_info_system.api.ReviewAPI;
import movie_info_system.dao.MovieDAO;
import movie_info_system.dao.ReviewDAO;
import movie_info_system.dto.ReviewDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewPanel extends JPanel {
    private JTextArea inputArea;
    private JButton saveButton;
    private JPanel reviewContainer;
    private JScrollPane reviewScroll;
    private String currentMovieTitle = null;

    /**
     * 생성자 - 리뷰 입력창, 저장 버튼, 리뷰 리스트 구성
     */
    public ReviewPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5), "리뷰"));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // 입력 영역 설정
        inputArea = new JTextArea(3, 40);
        inputArea.setMargin(new Insets(5, 5, 5, 5));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(inputScroll);

        add(Box.createVerticalStrut(5));

        // 저장 버튼 설정
        saveButton = new JButton("리뷰 저장");
        saveButton.setMaximumSize(new Dimension(100, 30));
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(saveButton);

        add(Box.createVerticalStrut(10));

        // 리뷰 목록 컨테이너 (카드형 표시)
        reviewContainer = new JPanel();
        reviewContainer.setLayout(new BoxLayout(reviewContainer, BoxLayout.Y_AXIS));
        reviewContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        reviewScroll = new JScrollPane(reviewContainer);
        reviewScroll.setPreferredSize(new Dimension(400, 200));
        reviewScroll.setMinimumSize(new Dimension(200, 100));
        reviewScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        reviewScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(reviewScroll);

        // 저장 버튼 클릭 시 - 리뷰 저장 및 갱신
        saveButton.addActionListener((ActionEvent e) -> {
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

                try {
                    reviewDAO.insertReview(dto);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // ✅ 저장 후 DB + API 리뷰 다시 불러오기
                List<ReviewDTO> myReviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);
                ReviewAPI reviewAPI = new ReviewAPI();
                List<ReviewDTO> apiReviews = reviewAPI.getMovieReviews(movieId, 1);

                List<ReviewDTO> combined = new ArrayList<>();
                combined.addAll(myReviews);
                combined.addAll(apiReviews);

                setReviews(combined); // 다시 전체 리뷰 보여주기
            }
        });

    }

    /**
     * 영화 제목 설정 → 해당 영화의 DB 리뷰 불러오기
     */
    public void setMovie(String title) {
        this.currentMovieTitle = title;

        MovieDAO movieDAO = new MovieDAO();
        Integer movieId = movieDAO.getMovieIdByTitle(title);

        if (movieId == null) {
            reviewContainer.removeAll();
            reviewContainer.revalidate();
            reviewContainer.repaint();
            return;
        }

        ReviewDAO reviewDAO = new ReviewDAO();
        List<ReviewDTO> reviews = reviewDAO.getLatestReviewsByMovieId(movieId, 10);
        setReviews(reviews);
    }

    /**
     * 리뷰 리스트 화면에 카드 형태로 출력
     */
    public void setReviews(List<ReviewDTO> reviews) {
        reviewContainer.removeAll();
        System.out.println("setReviews 호출, 데이터 수: " + reviews.size());

        for (ReviewDTO r : reviews) {
            boolean isMine = "guest".equalsIgnoreCase(r.getReviewer());
            String label = isMine ? "[내 리뷰]" : "[TMDB 리뷰]";
            String fullText = label + "\n" + r.getReviewer() + ":\n" + r.getContent();

            JTextArea reviewBox = new JTextArea(fullText);
            reviewBox.setLineWrap(true);
            reviewBox.setWrapStyleWord(true);
            reviewBox.setEditable(false);
            reviewBox.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
            reviewBox.setBackground(isMine ? new Color(230, 255, 230) : new Color(245, 245, 245));
            reviewBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(isMine ? Color.GREEN.darker() : Color.GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            reviewBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            reviewContainer.add(reviewBox);
            reviewContainer.add(Box.createVerticalStrut(5));
        }

        reviewContainer.revalidate();
        reviewContainer.repaint();
    }
}
