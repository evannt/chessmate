package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess.Piece;
import game.GameMode;
import gui.ChessBoardPainter;
import gui.ChessFrame;

public class MainMenu extends JPanel {

	private static final long serialVersionUID = -4905765460781590696L;

	public static final int SCREEN_WIDTH = ChessBoardPainter.TILE_SIZE * 15;
	public static final int SCREEN_HEIGHT = ChessBoardPainter.TILE_SIZE * 11;

	private static final int BUTTON_WIDTH = 250;
	private static final int BUTTON_HEIGHT = 50;

	private static final String GAME_TITLE = "CHESSMATE";

	private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 60);
	private static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 20);

	public MainMenu(ChessFrame parent) {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setDoubleBuffered(true); // improve game rendering performance
		setFocusable(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createVerticalStrut(100));

		JLabel titleLabel = new JLabel(GAME_TITLE);
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(ChessFrame.TAN);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleLabel);

		add(Box.createVerticalStrut(100));

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

		JButton playFriendButton = createMenuButton("Play a Friend");
		playFriendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int playerColor = getPlayerColor();
				if (playerColor != Piece.WHITE && playerColor != Piece.BLACK) {
					parent.switchPanel(ChessFrame.MAIN_MENU);
				} else {
					parent.startGame(GameMode.PLAY_FRIEND, playerColor);
					parent.switchPanel(ChessFrame.CHESS_PANEL);
				}
			}
		});

		JButton playBotButton = createMenuButton("Play a Bot");
		playBotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int playerColor = getPlayerColor();
				if (playerColor != Piece.WHITE && playerColor != Piece.BLACK) {
					parent.switchPanel(ChessFrame.MAIN_MENU);
				} else {
					parent.startGame(GameMode.PLAY_BOT, playerColor);
					parent.switchPanel(ChessFrame.CHESS_PANEL);
				}
			}
		});

		JButton exitButton = createMenuButton("Exit Game");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		buttonsPanel.add(playFriendButton);
		buttonsPanel.add(Box.createVerticalStrut(20));
		buttonsPanel.add(playBotButton);
		buttonsPanel.add(Box.createVerticalStrut(20));
		buttonsPanel.add(exitButton);

		add(buttonsPanel);

		setVisible(true);
	}

	public int getPlayerColor() {
		ChessColorSelector colorSelector = new ChessColorSelector();
		int selectedColor = colorSelector.getSelectedColor();

		return selectedColor;
	}

	private JButton createMenuButton(String text) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setForeground(ChessFrame.TAN);
		button.setBackground(ChessFrame.LIGHT_GREEN);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

		button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(ChessFrame.LIGHT_GREEN_ALT);
			}

			public void mouseExited(MouseEvent evt) {
				button.setBackground(ChessFrame.LIGHT_GREEN);
			}
		});

		return button;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D graphics2D = (Graphics2D) g.create();
		GradientPaint gradient = new GradientPaint(0, 0, ChessFrame.DARK_GREEN, 0, getHeight(),
				ChessFrame.DARK_GREEN_ALT);
		graphics2D.setPaint(gradient);
		graphics2D.fillRect(0, 0, getWidth(), getHeight());
	}
}
