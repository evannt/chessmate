package ui;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import chess.MoveLog;
import gui.ChessBoardPainter;

public class UI {

	private static final int MOVE_LOG_X = 11 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_Y = ChessBoardPainter.START_RANK * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_WIDTH = 3 * ChessBoardPainter.TILE_SIZE;
	private static final int MOVE_LOG_HEIGHT = 6 * ChessBoardPainter.TILE_SIZE;

	public JScrollPane moveLogPane;
	public JTextPane moveLogDisplay;

	public UI() {
		moveLogDisplay = new JTextPane();
		moveLogDisplay.setContentType("text/html");
		moveLogDisplay.setEditable(false);
		moveLogDisplay.setFocusable(false);
		moveLogDisplay.setBounds(MOVE_LOG_X, MOVE_LOG_Y, MOVE_LOG_WIDTH, MOVE_LOG_HEIGHT);

		moveLogPane = new JScrollPane(moveLogDisplay);
		moveLogPane.setOpaque(true);
		moveLogPane.setBounds(moveLogDisplay.getBounds());
		moveLogPane.setBorder(BorderFactory.createLineBorder(ChessBoardPainter.DARK_GRAY_ALT, 10));
	}

	public void updateMoveLog(MoveLog moveLog) {
		moveLogDisplay.setText(moveLog.getLog());
	}

}
