package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import chess.Piece;
import chess.Position;
import util.BoardUtil;

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

	// Colors and font used on the board
	public static final Color DARK_GRAY = new Color(47, 47, 47);
	public static final Color DARK_GRAY_ALT = new Color(65, 65, 65);
	public static final Color TAN = new Color(238, 238, 210);
	public static final Color GREEN = new Color(118, 150, 86);
	public static final Font GEOGRIA_11 = new Font("Arial", Font.BOLD, 11);

	private Position position;
	private int selectedSquare;
	private int activeSquare;

	public ChessBoardPainter() {
		position = new Position();
		selectedSquare = -1;
		activeSquare = -1;
//		position.setPosition(Position.START_POSITION);
		position.setPosition("rnbqkbnr/ppp1pppp/3p4/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setSelectedSquare(int selectedSquare) {
		this.selectedSquare = selectedSquare;
	}

	public void setActiveSquare(int activeSquare) {
		this.activeSquare = activeSquare;
	}

	public boolean isHighlightable(int square) {
		return position.getPiece(square) != null;
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

	public void drawPieces(Graphics2D graphics2D) {
		for (int i = 0; i < 64; i++) {
			Piece p = position.getPiece(i);
//			System.out.println("DRAW");
			if (p != null) {
				graphics2D.drawImage(p.getImage(), p.getX(), p.getY(), TILE_SIZE, TILE_SIZE, null);
			}
		}
	}

	public void highlightSelectedSquare(Graphics2D graphics2D) {
		if (selectedSquare != -1) {
			int rank = BoardUtil.getRankFromIndex(selectedSquare);
			int file = BoardUtil.getFileFromIndex(selectedSquare);
//			System.out.println("Rank: " + rank + ", File: " + file);
			int x = (file + START_FILE) * TILE_SIZE;
			int y = (rank + START_RANK) * TILE_SIZE;
			graphics2D.setColor(new Color(255, 255, 0, 92));
			graphics2D.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		}
	}

	public void mousePressed(MouseEvent e, int square) {
		if (selectedSquare == -1) { // Attempting to selecte a piece
			if (position.hasPiece(square)) {
				setSelectedSquare(square);
			}
		}
	}

	public void mouseReleased(MouseEvent e, int square) {
		if (selectedSquare == square) {
			if (activeSquare != square && activeSquare != -1) {
//				System.out.println("active sq: " + activeSquare);
				movePiece(activeSquare, square);
				setSelectedSquare(-1);
				setActiveSquare(-1);
			} else {
//				System.out.println("Setting active sq");
				setActiveSquare(square);
			}
		} else if (selectedSquare != square && activeSquare != -1) {
			// attempt to move
//			System.out.println("Attempting to move: " + selectedSquare);
			movePiece(selectedSquare, square);
			setSelectedSquare(-1);
			setActiveSquare(-1);
		}

		// reset the piece location after moving
		position.drawPieces();
		System.out.println(position.getFenString());
	}

	public void mouseDragged(MouseEvent e) {
		if (selectedSquare != -1) {
			setActiveSquare(selectedSquare);
			Piece p = position.getPiece(selectedSquare);
			if (p != null) {
				p.setX(e.getX() - (TILE_SIZE / 2));
				p.setY(e.getY() - (TILE_SIZE / 2));
			}
		}
	}

	private void movePiece(int from, int to) {
		int rank = BoardUtil.getRankFromIndex(to);
		int file = BoardUtil.getFileFromIndex(to);
		Piece p = position.getPiece(activeSquare);
//		System.out.println("Piece: " + p);
		if (p != null) {
			p.setX((file + START_FILE) * TILE_SIZE);
			p.setY((rank + START_RANK) * TILE_SIZE);
		}
		position.makeMove(0, null);
		position.movePiece(from, to);
	}

}
