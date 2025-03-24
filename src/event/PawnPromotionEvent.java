package event;

import chess.PieceType;

public class PawnPromotionEvent extends ChessEvent {

	private PieceType promotedPiece;
	private int color;

	public PawnPromotionEvent(int targetSquare, int color) {
		super(targetSquare);
		this.color = color;
	}

	public void setPromotionResponse(PieceType promotedPiece) {
		this.promotedPiece = promotedPiece;
	}

	public PieceType getPromotedPiece() {
		return promotedPiece;
	}

	public int getColor() {
		return color;
	}

}
