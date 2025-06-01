package movie_info_system.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame {

	private JFrame frame;
	private JTextField searchField;
	private JTable table;
	private DefaultTableModel tableModel;
	private MovieDetailPanel detailPanel;
	private FavoritesPanel favoritesPanel;

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

		// 🔍 상단 검색 패널
		JPanel searchPanel = new JPanel();
		searchField = new JTextField(30);
		JButton searchButton = new JButton("검색");

		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		frame.getContentPane().add(searchPanel, BorderLayout.NORTH);

		// 📋 중앙 테이블
		tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "제목", "개봉일", "평점" });
		table = new JTable(tableModel);
		JScrollPane tableScrollPane = new JScrollPane(table);
		frame.getContentPane().add(tableScrollPane, BorderLayout.CENTER);

		// 📌 우측 패널 (상세정보 + 즐겨찾기)
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		frame.getContentPane().add(rightPanel, BorderLayout.EAST);

		// 상세 정보 패널
		detailPanel = new MovieDetailPanel();
		rightPanel.add(detailPanel);

		// 즐겨찾기 패널
		favoritesPanel = new FavoritesPanel();
		rightPanel.add(favoritesPanel);

		// ⭐ 즐겨찾기 추가 버튼
		JButton favButton = new JButton("즐겨찾기 추가");
		detailPanel.add(favButton);

		favButton.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				String title = table.getValueAt(row, 0).toString();
				favoritesPanel.addFavorite(title);
			}
		});

		// 🔁 검색 버튼 클릭 시 더미 데이터로 테이블 갱신
		searchButton.addActionListener(e -> {
			String query = searchField.getText().trim();
			if (!query.isEmpty()) {
				tableModel.setRowCount(0); // 기존 데이터 삭제
				// 예시 데이터 삽입
				tableModel.addRow(new Object[] { query + " 영화1", "2023-01-01", "8.5" });
				tableModel.addRow(new Object[] { query + " 영화2", "2022-06-15", "7.8" });
			}
		});

		// 🎯 테이블 클릭 시 상세정보 갱신
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				if (row != -1) {
					String title = table.getValueAt(row, 0).toString();
					String date = table.getValueAt(row, 1).toString();
					String rating = table.getValueAt(row, 2).toString();
					detailPanel.setMovieDetail(title, date, rating, "줄거리 예시입니다.");
				}
			}
		});
	}
}