package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import chess.MoveLog;
import gui.ChessBoardPainter;
import gui.ChessFrame;

public class UI {

	private static final int MOVE_LOG_X = 11 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_WIDTH = 3 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_HEIGHT = 7 * ChessBoardPainter.TILE_SIZE;

	private static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 14);
	private static final int BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 30;

	private static final int REMATCH_BUTTON_X = (11 * ChessBoardPainter.TILE_SIZE) - (15);
	private static final int REMATCH_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (7 * ChessBoardPainter.TILE_SIZE + ChessBoardPainter.TILE_SIZE / 2);
	private static final int NEW_GAME_BUTTON_X = REMATCH_BUTTON_X + BUTTON_WIDTH
			+ (BUTTON_HEIGHT);
	private static final int NEW_GAME_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (7 * ChessBoardPainter.TILE_SIZE + ChessBoardPainter.TILE_SIZE / 2);

	public JButton rematchButton;
	public JButton newGameButton;

	public JScrollPane moveLogPane;
	public JTextPane moveLogDisplay;

	public UI() {
		rematchButton = createUIButton("Rematch");
		newGameButton = createUIButton("New Game");
		rematchButton.setBounds(REMATCH_BUTTON_X, REMATCH_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		newGameButton.setBounds(NEW_GAME_BUTTON_X, NEW_GAME_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);

		moveLogDisplay = new JTextPane();
		moveLogDisplay.setContentType("text/html");
		moveLogDisplay.setEditable(false);
		moveLogDisplay.setFocusable(false);
		moveLogDisplay.setBounds(MOVE_LOG_X, MOVE_LOG_Y, MOVE_LOG_WIDTH, MOVE_LOG_HEIGHT);
		moveLogDisplay.setMargin(new Insets(0, 0, 0, 0));
		moveLogDisplay.setBackground(ChessFrame.DARK_GRAY_ALT);

		moveLogPane = new JScrollPane(moveLogDisplay);
		moveLogPane.setOpaque(true);
		moveLogPane.setBounds(moveLogDisplay.getBounds());
		moveLogPane.setBorder(BorderFactory.createLineBorder(ChessFrame.DARK_GRAY_ALT, 10));
		moveLogPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		moveLogPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollBar verticalScrollBar = moveLogPane.getVerticalScrollBar();
		verticalScrollBar.setUI(new BasicScrollBarUI() {
			@Override
			public Dimension getPreferredSize(JComponent c) {
				return new Dimension(5, super.getPreferredSize(c).height);
			}

			@Override
			protected void configureScrollBarColors() {
				thumbColor = ChessFrame.LIGHT_GRAY;
				trackColor = ChessFrame.DARK_GRAY;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createBlankButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createBlankButton();
			}

			private JButton createBlankButton() {
				JButton button = new JButton();
				button.setPreferredSize(new Dimension(0, 0));
				button.setMinimumSize(new Dimension(0, 0));
				button.setMaximumSize(new Dimension(0, 0));
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				return button;
			}

		});
	}

	public void updateMoveLog(MoveLog moveLog) {
		moveLogDisplay.setText(moveLog.getLog());
	}

	private JButton createUIButton(String text) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setForeground(ChessFrame.DARK_GRAY);
		button.setBackground(ChessFrame.LIGHT_GRAY);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

		button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(ChessFrame.DARK_GRAY_ALT);
			}

			public void mouseExited(MouseEvent evt) {
				button.setBackground(ChessFrame.LIGHT_GRAY);
			}
		});

		return button;
	}

}
