package movie_info_system.gui;

import javax.swing.*;
import java.awt.*;

public class MovieDetailPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel dateLabel;
    private JLabel ratingLabel;
    private JTextArea overviewArea;
    private JLabel posterLabel;

    public MovieDetailPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("영화 상세 정보"));

        // 📌 포스터 이미지용 JLabel
        posterLabel = new JLabel();
        posterLabel.setPreferredSize(new Dimension(150, 225));
        posterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(posterLabel, BorderLayout.WEST);

        // 📌 영화 정보 텍스트들 (우측 영역)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("제목: ");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateLabel = new JLabel("개봉일: ");
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ratingLabel = new JLabel("평점: ");
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel overviewLabel = new JLabel("줄거리:");
        overviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);  // ✅ 줄거리 라벨 왼쪽 정렬

        overviewArea = new JTextArea(5, 20);
        overviewArea.setLineWrap(true);
        overviewArea.setWrapStyleWord(true);
        overviewArea.setEditable(false);
        JScrollPane overviewScroll = new JScrollPane(overviewArea);
        overviewScroll.setAlignmentX(Component.LEFT_ALIGNMENT); // ✅ 스크롤 영역 왼쪽 정렬

        // ✅ 순서대로 추가
        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(ratingLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(overviewLabel);
        infoPanel.add(overviewScroll);

        add(infoPanel, BorderLayout.CENTER);
    }

    public void setMovieDetail(String title, String date, String rating, String overview, ImageIcon poster) {
        titleLabel.setText("제목: " + title);
        dateLabel.setText("개봉일: " + date);
        ratingLabel.setText("평점: " + rating);
        overviewArea.setText(overview);

        if (poster != null) {
            Image img = poster.getImage().getScaledInstance(150, 225, Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(img));
        } else {
            posterLabel.setIcon(null);
        }
    }
}
