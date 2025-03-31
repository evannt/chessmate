package event;

import chess.PieceType;

public class PawnPromotionEvent implements ChessEvent {

	private PieceType promotedPiece;
	private int targetSquare;
	private int color;

	public PawnPromotionEvent(int targetSquare, int color) {
		this.targetSquare = targetSquare;
		this.color = color;
	}

	public void setPromotionResponse(PieceType promotedPiece) {
		this.promotedPiece = promotedPiece;
	}

	public PieceType getPromotedPiece() {
		return promotedPiece;
	}

	public int getTargetSquare() {
		return targetSquare;
	}

	public int getColor() {
		return color;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.PAWN_PROMOTION;
	}

}
