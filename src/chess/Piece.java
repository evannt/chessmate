package chess;

import java.awt.image.BufferedImage;

import util.BoardUtil;
import util.ImageUtil;

public class Piece {

	public static final String PIECE_PATH = "resc/pieces/";
	public static final String BLACK_PIECE_EXTENSION = "b.png";
	public static final String WHITE_PIECE_EXTENSION = "w.png";

	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int BOTH = 2;

	private BufferedImage image;

	private PieceType type;

	private int x;
	private int y;

	private int flippedX;
	private int flippedY;

	public Piece(PieceType type) {
		this.type = type;
		this.image = ImageUtil.getImage(generatePath());
	}

	public Piece(Piece other) {
		this.x = other.x;
		this.y = other.y;
		this.flippedX = BoardUtil.translateX(x);
		this.flippedY = BoardUtil.translateY(y);
		this.type = other.type;
		this.image = other.image;
	}

	public PieceType getPieceType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		this.flippedX = BoardUtil.translateX(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
		this.flippedY = BoardUtil.translateY(y);
	}

	public int getFlippedX() {
		return flippedX;
	}

	public int getFlippedY() {
		return flippedY;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean isWhite() {
		return type.isWhite();
	}

	private String generatePath() {
		String colorExtension = isWhite() ? WHITE_PIECE_EXTENSION : BLACK_PIECE_EXTENSION;
		return PIECE_PATH + type.getId() + colorExtension;
	}

}
