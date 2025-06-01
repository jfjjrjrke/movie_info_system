package movie_info_system.gui;

import javax.swing.*;
import java.awt.*;

public class FavoritesPanel extends JPanel {

	private DefaultListModel<String> listModel;
	private JList<String> favoriteList;
	private JButton removeButton;

	public FavoritesPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder("즐겨찾기 목록"));

		listModel = new DefaultListModel<>();
		favoriteList = new JList<>(listModel);
		add(new JScrollPane(favoriteList), BorderLayout.CENTER);

		removeButton = new JButton("삭제");
		add(removeButton, BorderLayout.SOUTH);

		removeButton.addActionListener(e -> {
			int index = favoriteList.getSelectedIndex();
			if (index != -1) {
				listModel.remove(index);
			}
		});
	}

	public void addFavorite(String movieTitle) {
		if (!listModel.contains(movieTitle)) {
			listModel.addElement(movieTitle);
		}
	}
}
