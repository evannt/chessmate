package ui;

import java.awt.Dimension;
import java.awt.Insets;

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

	public JScrollPane moveLogPane;
	public JTextPane moveLogDisplay;

	public UI() {
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

}
