package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.SwingUtilities;

import chess.Move;
import chess.Piece;
import chess.PieceType;
import chess.Position;
import event.ChessEvent;
import event.ChessEventListener;
import event.PawnPromotionEvent;
import ui.PawnPromotionPrompt;
import util.BoardUtil;

public class ChessBoardPainter implements ChessEventListener {

	// Chess square sizes and location
	public static final int TILE_SIZE = 72;
	public static final int SCALED_TILE_SIZE = TILE_SIZE * 5;
	public static final int START_RANK = 2;
	public static final int START_FILE = 2;

	// Border location and size
	private static final int BORDER_X = (START_FILE * TILE_SIZE) - (TILE_SIZE / 6);
	private static final int BORDER_Y = (START_RANK * TILE_SIZE) - (TILE_SIZE / 6);
	private static final int BORDER_SIZE = (TILE_SIZE * 8) + ((int) (TILE_SIZE / 2.8));

	public static final int QUEEN_PROMOTION = 0;
	public static final int KNIGHT_PROMOTION = 1;
	public static final int ROOK_PROMOTION = 2;
	public static final int BISHOP_PROMOTION = 3;

	// Colors and font used on the board
	public static final Color DARK_GRAY = new Color(47, 47, 47);
	public static final Color DARK_GRAY_ALT = new Color(65, 65, 65);
	public static final Color UI_COLOR = Color.gray;
	public static final Color TAN = new Color(238, 238, 210);
	public static final Color GREEN = new Color(118, 150, 86);
	public static final Color HIGHLIGHT = new Color(255, 255, 0, 92);
	public static final Font ARIAL_11 = new Font("Arial", Font.BOLD, 11);

	private ChessPanel chessPanel;

	public ChessBoardPainter(ChessPanel chessPanel) {
		this.chessPanel = chessPanel;
	}

	public void drawBorder(Graphics2D graphics2D) {
		graphics2D.setColor(DARK_GRAY_ALT);
		graphics2D.fillRect(BORDER_X, BORDER_Y, BORDER_SIZE, BORDER_SIZE);
	}

	public void drawBoard(Graphics2D graphics2D) {
		for (int rank = START_RANK; rank < START_RANK + 8; rank++) {
			for (int file = START_FILE; file < START_FILE + 8; file++) {
				if ((rank + file) % 2 == 0) {
					graphics2D.setColor(GREEN);
				} else {
					graphics2D.setColor(TAN);
				}
				graphics2D.fillRect(file * TILE_SIZE, rank * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				graphics2D.setFont(ARIAL_11);
				graphics2D.setColor(Color.DARK_GRAY);
				if (Math.abs(rank - START_RANK) == 7) {
					graphics2D.drawString(BoardUtil.getFileAsLetter(file - START_FILE),
							(file * TILE_SIZE) + (int) (TILE_SIZE / 1.125),
							(rank * TILE_SIZE) + (int) (TILE_SIZE / 1.025));
				}
				if (file == START_FILE) {
					graphics2D.drawString(String.valueOf(BoardUtil.getTrueRank(rank - START_RANK)),
							(file * TILE_SIZE) + TILE_SIZE / 18, (rank * TILE_SIZE) + TILE_SIZE / 5);

				}
			}
		}
	}

	public void drawPieces(Graphics2D graphics2D, Position position) {
		for (int i = 0; i < 64; i++) {
			Piece p = position.getPiece(i);
			if (p != null && p.getPieceType() != PieceType.NONE) {
				// TODO Bound pieces to the board region
				int x = p.getX();
				int y = p.getY();
				graphics2D.drawImage(p.getImage(), x, y, TILE_SIZE, TILE_SIZE, null);
			}
		}
	}

	public PieceType drawPiecePromotionPrompt(int color, int square) {
		int rank = BoardUtil.getRankFromIndex(square);
		int file = BoardUtil.getFileFromIndex(square);
		int x = (file + START_FILE) * TILE_SIZE;
		int y = (rank + START_RANK) * TILE_SIZE;
		Point point = new Point(x, y);
		SwingUtilities.convertPointToScreen(point, chessPanel);
		PawnPromotionPrompt prompt = new PawnPromotionPrompt(color, point.x, point.y);

		return prompt.getPromotionSelection();
	}

	public void highlightSelectedSquare(Graphics2D graphics2D, int selectedSquare) {
		if (selectedSquare != -1) {
			int rank = BoardUtil.getRankFromIndex(selectedSquare);
			int file = BoardUtil.getFileFromIndex(selectedSquare);
			int x = (file + START_FILE) * TILE_SIZE;
			int y = (rank + START_RANK) * TILE_SIZE;
			graphics2D.setColor(HIGHLIGHT);
			graphics2D.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		}
	}

	public void highlightMove(Graphics2D graphics2D, int move) {
		if (move != -1) {
			int src = Move.getSrc(move);
			int srcX = (BoardUtil.getFileFromIndex(src) + START_FILE) * TILE_SIZE;
			int srcY = (BoardUtil.getRankFromIndex(src) + START_RANK) * TILE_SIZE;
			graphics2D.setColor(HIGHLIGHT);
			graphics2D.fillRect(srcX, srcY, TILE_SIZE, TILE_SIZE);
			int dst = Move.getDst(move);
			int dstX = (BoardUtil.getFileFromIndex(dst) + START_FILE) * TILE_SIZE;
			int dstY = (BoardUtil.getRankFromIndex(dst) + START_RANK) * TILE_SIZE;
			graphics2D.setColor(HIGHLIGHT);
			graphics2D.fillRect(dstX, dstY, TILE_SIZE, TILE_SIZE);
		}
	}

	@Override
	public void update(ChessEvent event) {
		if (event instanceof PawnPromotionEvent promotionEvent) {
			int color = promotionEvent.getColor();
			int square = promotionEvent.getTargetSquare();
			promotionEvent.setPromotionResponse(drawPiecePromotionPrompt(color, square));
		}
	}

}
