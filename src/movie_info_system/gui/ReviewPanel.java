package movie_info_system.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ReviewPanel extends JPanel {
    private JTextArea inputArea;
    private JButton saveButton;
    private DefaultListModel<String> reviewListModel;
    private JList<String> reviewList;

    // 영화 제목별 리뷰 저장용 (임시)
    private final java.util.Map<String, java.util.List<String>> reviewMap = new java.util.HashMap<>();
    private String currentMovieTitle = null;

    public ReviewPanel() {
        setLayout(new BorderLayout());

        // 입력 영역
        inputArea = new JTextArea(3, 40);
        add(new JScrollPane(inputArea), BorderLayout.NORTH);

        // 저장 버튼
        saveButton = new JButton("리뷰 저장");
        add(saveButton, BorderLayout.CENTER);

        // 리뷰 리스트
        reviewListModel = new DefaultListModel<>();
        reviewList = new JList<>(reviewListModel);
        add(new JScrollPane(reviewList), BorderLayout.SOUTH);

        // 저장 버튼 클릭 시
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMovieTitle != null && !inputArea.getText().trim().isEmpty()) {
                    String review = inputArea.getText().trim();
                    inputArea.setText("");

                    reviewMap.putIfAbsent(currentMovieTitle, new ArrayList<>());
                    reviewMap.get(currentMovieTitle).add(review);

                    refreshReviewList(currentMovieTitle);
                }
            }
        });
    }

    public void setMovie(String title) {
        currentMovieTitle = title;
        refreshReviewList(title);
    }

    private void refreshReviewList(String title) {
        reviewListModel.clear();
        if (reviewMap.containsKey(title)) {
            for (String review : reviewMap.get(title)) {
                reviewListModel.addElement(review);
            }
        }
    }
}