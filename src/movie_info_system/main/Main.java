package movie_info_system.main;

import java.awt.EventQueue;

import movie_info_system.gui.MainFrame;

public class Main {

	public static void main(String[] args) {
		 EventQueue.invokeLater(() -> {
	            MainFrame gui = new MainFrame();         // GUI 생성
	            gui.getFrame().setVisible(true);         // ✅ getter로 프레임 표시
	        });
		 
	}

}
