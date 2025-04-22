package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import gui.ChessFrame;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> new ChessFrame()); // display game screen
	}

}
