package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

import chess.MoveLog;
import chess.Piece;
import gui.ChessBoardPainter;
import gui.ChessFrame;

public class UI {
	private static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 14);
	private static final int SMALL_BUTTON_WIDTH = 50;
	private static final int SMALL_BUTTON_HEIGHT = 30;
	private static final int BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 30;
	private static final int TURN_INDICATOR_WIDTH = 90;
	private static final int TURN_INDICATOR_HEIGHT = 90;

	private static final int MOVE_LOG_X = 11 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_WIDTH = 3 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_HEIGHT = 7 * ChessBoardPainter.TILE_SIZE;

	private static final int TURN_INDICATOR_X = ChessBoardPainter.TILE_SIZE / 4;
	private static final int TURN_INDICATOR_Y = 5 * ChessBoardPainter.TILE_SIZE;

	private static final int UNDO_BUTTON_X = 11 * ChessBoardPainter.TILE_SIZE;
	private static final int UNDO_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (MOVE_LOG_HEIGHT + ChessBoardPainter.TILE_SIZE / 4);
	private static final int REDO_BUTTON_X = MOVE_LOG_X + MOVE_LOG_WIDTH - SMALL_BUTTON_WIDTH;
	private static final int REDO_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (MOVE_LOG_HEIGHT + ChessBoardPainter.TILE_SIZE / 4);

	private static final int REMATCH_BUTTON_X = (11 * ChessBoardPainter.TILE_SIZE) - (15);
	private static final int REMATCH_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (MOVE_LOG_HEIGHT + ChessBoardPainter.TILE_SIZE);
	private static final int NEW_GAME_BUTTON_X = REMATCH_BUTTON_X + BUTTON_WIDTH
			+ (BUTTON_HEIGHT);
	private static final int NEW_GAME_BUTTON_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE
			+ (MOVE_LOG_HEIGHT + ChessBoardPainter.TILE_SIZE);

	public final JPanel turnIndicatorDisplay;
	public final JPanel turnIndicator;
	public final JLabel turnText;

	public final JButton undoButton;
	public final JButton redoButton;

	public final JButton rematchButton;
	public final JButton newGameButton;

	public final JScrollPane moveLogPane;
	public final JTextPane moveLogDisplay;

	public UI() {
		turnIndicatorDisplay = new JPanel(new BorderLayout(15, 10));
		turnIndicator = new JPanel();
		turnText = new JLabel();
		turnText.setForeground(Color.WHITE);
		turnIndicator.setToolTipText("Turn");
		turnText.setHorizontalAlignment(JLabel.CENTER);
		turnIndicatorDisplay.add(turnText, BorderLayout.NORTH);
		turnIndicatorDisplay.add(turnIndicator, BorderLayout.CENTER);
		turnIndicatorDisplay.setBackground(ChessFrame.DARK_GRAY_ALT);
		turnIndicatorDisplay.setBorder(BorderFactory.createLineBorder(ChessFrame.DARK_GRAY_ALT, 5));
//		turnIndicatorDisplay.setOpaque(false);
		turnIndicatorDisplay.setBounds(TURN_INDICATOR_X, TURN_INDICATOR_Y, TURN_INDICATOR_WIDTH, TURN_INDICATOR_HEIGHT);

		undoButton = createUIButton("<");
		redoButton = createUIButton(">");
		undoButton.setBounds(UNDO_BUTTON_X, UNDO_BUTTON_Y, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT);
		redoButton.setBounds(REDO_BUTTON_X, REDO_BUTTON_Y, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT);

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

	public void updateTurnIndicator(int color) {
		turnText.setText((color == Piece.WHITE ? "White's" : "Black's") + " Turn");
		turnIndicator.setBackground(color == Piece.WHITE ? Color.white : Color.black);
	}

	private JButton createUIButton(String text) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setForeground(ChessFrame.DARK_GRAY);
		button.setBackground(ChessFrame.LIGHT_GRAY);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

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
