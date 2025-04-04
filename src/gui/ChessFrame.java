package gui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.GameMode;
import ui.MainMenu;

public class ChessFrame extends JFrame {

	private static final long serialVersionUID = -2536492765344084132L;

	public static final Color DARK_GRAY = new Color(47, 47, 47);
	public static final Color DARK_GRAY_ALT = new Color(65, 65, 65);
	public static final Color LIGHT_GRAY = new Color(85, 85, 85);
	public static final Color DARK_GREEN = new Color(24, 28, 20);
	public static final Color DARK_GREEN_ALT = new Color(64, 68, 60);
	public static final Color MID_GREEN = new Color(60, 61, 55);
	public static final Color LIGHT_GREEN = new Color(105, 117, 101);
	public static final Color LIGHT_GREEN_ALT = new Color(145, 157, 141);
	public static final Color TAN = new Color(236, 223, 204);

	public static final String MAIN_MENU = "Main Menu";
	public static final String CHESS_PANEL = "Chess Panel";

	private CardLayout cardLayout;
	private JPanel panels;

	private MainMenu mainMenu;
	private ChessPanel chessPanel;

	public ChessFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Chess");

		mainMenu = new MainMenu(this);
		chessPanel = new ChessPanel();

		cardLayout = new CardLayout();
		panels = new JPanel(cardLayout);
		panels.add(mainMenu, MAIN_MENU);
		panels.add(chessPanel, CHESS_PANEL);

		add(panels);

		cardLayout.show(panels, MAIN_MENU);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void startGame(GameMode gameMode, int playerColor) {
		chessPanel.setupGame(gameMode, playerColor);
	}

	public void switchPanel(String panelName) {
		cardLayout.show(panels, panelName);
	}

}
