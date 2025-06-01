package movie_info_system.gui;

import javax.swing.*;

import movie_info_system.dao.FavoriteDAO;
import movie_info_system.dao.MovieDAO;
import movie_info_system.dto.MovieDTO;

import java.util.List;
import java.awt.*;

public class FavoritesPanel extends JPanel {

	private String userId;
    private DefaultListModel<String> listModel;
    private JList<String> favoriteList;
    private JButton removeButton;

    public FavoritesPanel(String userId) {
    	 this.userId = userId; // 👈 외부에서 전달된 userId 저장
    	
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("즐겨찾기 목록"));

        listModel = new DefaultListModel<>();
        favoriteList = new JList<>(listModel);
        add(new JScrollPane(favoriteList), BorderLayout.CENTER);

        removeButton = new JButton("삭제");
        add(removeButton, BorderLayout.SOUTH);

        removeButton.addActionListener(e -> {
        	 String selectedTitle = favoriteList.getSelectedValue();
        	    if (selectedTitle != null) {
        	        MovieDAO movieDAO = new MovieDAO();
        	        Integer movieId = movieDAO.getMovieIdByTitle(selectedTitle);
        	        if (movieId != null) {
        	            FavoriteDAO favoriteDAO = new FavoriteDAO();
        	            boolean success = favoriteDAO.removeFavorite(movieId, userId);
        	            if (success) {
        	                listModel.removeElement(selectedTitle);
        	            } else {
        	                JOptionPane.showMessageDialog(this, "삭제에 실패했습니다.");
        	            }
        	        }
        	    }
        });
    }

    public void addFavorite(String movieTitle) {
        if (!listModel.contains(movieTitle)) {
            listModel.addElement(movieTitle);
        }
    }

    // ✅ 추가: 외부에서 즐겨찾기 리스트 접근 가능하게
    public JList<String> getFavoritesList() {
        return favoriteList;
    }
    
 // FavoritesPanel.java 내부
    public void loadFavoritesFromDB() {
        listModel.clear();
        FavoriteDAO dao = new FavoriteDAO();
        List<MovieDTO> favorites = dao.getFavoriteMovies(userId);
        for (MovieDTO movie : favorites) {
            listModel.addElement(movie.getTitle());
        }
    }

}
