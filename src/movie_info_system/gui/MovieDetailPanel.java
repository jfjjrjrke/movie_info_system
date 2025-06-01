package movie_info_system.gui;

import javax.swing.*;
import java.awt.*;

public class MovieDetailPanel extends JPanel {

	private JLabel titleLabel;
	private JLabel dateLabel;
	private JLabel ratingLabel;
	private JTextArea overviewArea;

	public MovieDetailPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder("영화 상세 정보"));

		titleLabel = new JLabel("제목: ");
		dateLabel = new JLabel("개봉일: ");
		ratingLabel = new JLabel("평점: ");
		overviewArea = new JTextArea(5, 20);
		overviewArea.setLineWrap(true);
		overviewArea.setWrapStyleWord(true);
		overviewArea.setEditable(false);

		add(titleLabel);
		add(dateLabel);
		add(ratingLabel);
		add(new JScrollPane(overviewArea));
	}

	public void setMovieDetail(String title, String date, String rating, String overview) {
		titleLabel.setText("제목: " + title);
		dateLabel.setText("개봉일: " + date);
		ratingLabel.setText("평점: " + rating);
		overviewArea.setText(overview);
	}
}
