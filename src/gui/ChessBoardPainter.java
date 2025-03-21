package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import chess.Piece;
import chess.PieceType;
import chess.Position;
import util.BoardUtil;
import util.ImageUtil;

public class ChessBoardPainter {

	// Chess square sizes and location
	public static final int TILE_SIZE = 72;
	public static final int SCALED_TILE_SIZE = TILE_SIZE * 5;
	public static final int START_RANK = 2;
	public static final int START_FILE = 3;

	// Border location and size
	private static final int BORDER_X = (START_FILE * TILE_SIZE) - (TILE_SIZE / 6);
	private static final int BORDER_Y = (START_RANK * TILE_SIZE) - (TILE_SIZE / 6);
	private static final int BORDER_SIZE = (TILE_SIZE * 8) + ((int) (TILE_SIZE / 2.8));

	// Pawn promotion pieces
	private static BufferedImage queen;
	private static BufferedImage knight;
	private static BufferedImage rook;
	private static BufferedImage bishop;

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
	public static final Font GEOGRIA_11 = new Font("Arial", Font.BOLD, 11);

	public ChessBoardPainter(int playerColor) {
		if (playerColor == Piece.WHITE) {
			queen = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.WQUEEN.getId() + Piece.WHITE_PIECE_EXTENSION);
			rook = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.WROOK.getId() + Piece.WHITE_PIECE_EXTENSION);
			bishop = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.WBISHOP.getId() + Piece.WHITE_PIECE_EXTENSION);
			knight = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.WKNIGHT.getId() + Piece.WHITE_PIECE_EXTENSION);
		} else {
			queen = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.BQUEEN.getId() + Piece.BLACK_PIECE_EXTENSION);
			rook = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.BROOK.getId() + Piece.BLACK_PIECE_EXTENSION);
			bishop = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.BBISHOP.getId() + Piece.BLACK_PIECE_EXTENSION);
			knight = ImageUtil.getImage(Piece.PIECE_PATH + PieceType.BKNIGHT.getId() + Piece.BLACK_PIECE_EXTENSION);
		}
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
				graphics2D.setFont(GEOGRIA_11);
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

	public void drawPiecePromotionPrompt(Graphics2D graphics2D, int color, int square) {
		int rank = BoardUtil.getRankFromIndex(square);
		int file = BoardUtil.getFileFromIndex(square);
		int queenX = (file + START_FILE) * TILE_SIZE;
		int queenY = (rank + START_RANK) * TILE_SIZE;
		int knightX = (file + START_FILE) * TILE_SIZE;
		int knightY = (rank + START_RANK) * TILE_SIZE + TILE_SIZE;
		int rookX = (file + START_FILE) * TILE_SIZE;
		int rookY = (rank + START_RANK) * TILE_SIZE + (2 * TILE_SIZE);
		int bishopX = (file + START_FILE) * TILE_SIZE;
		int bishopY = (rank + START_RANK) * TILE_SIZE + (3 * TILE_SIZE);

		graphics2D.setColor(UI_COLOR);
		graphics2D.fillRect(queenX, queenY, TILE_SIZE, 4 * TILE_SIZE);
		graphics2D.drawImage(queen, queenX, queenY, TILE_SIZE, TILE_SIZE, null);
		graphics2D.drawImage(knight, knightX, knightY, TILE_SIZE, TILE_SIZE, null);
		graphics2D.drawImage(rook, rookX, rookY, TILE_SIZE, TILE_SIZE, null);
		graphics2D.drawImage(bishop, bishopX, bishopY, TILE_SIZE, TILE_SIZE, null);
	}

	public void highlightSelectedSquare(Graphics2D graphics2D, int selectedSquare) {
		if (selectedSquare != -1) {
			int rank = BoardUtil.getRankFromIndex(selectedSquare);
			int file = BoardUtil.getFileFromIndex(selectedSquare);
			int x = (file + START_FILE) * TILE_SIZE;
			int y = (rank + START_RANK) * TILE_SIZE;
			graphics2D.setColor(new Color(255, 255, 0, 92));
			graphics2D.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		}
	}

}
