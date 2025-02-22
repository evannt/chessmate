package chess;

import java.awt.image.BufferedImage;

import util.ImageUtil;

public class Piece {

	public static final String PIECE_PATH = "resc/pieces/";
	private static final String BLACK_PIECE_EXTENSION = "b.png";
	private static final String WHITE_PIECE_EXTENSION = "w.png";

	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int BOTH = 2;

	private BufferedImage image;

	private PieceType type;

	private int x;
	private int y;

	public Piece(PieceType type) {
		this.type = type;
		this.image = ImageUtil.getImage(generatePath());
	}

	public PieceType getPieceType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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
