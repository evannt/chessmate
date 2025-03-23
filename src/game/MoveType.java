package game;

import chess.Move;

public enum MoveType {

	NONE(""), NORMAL("move-self"), CAPTURE("capture"), CASTLE("castle"), PROMOTION("promote"), CHECK("move-check"),
	INVALID("notify");

	private String soundKey;

	private MoveType(String soundKey) {
		this.soundKey = soundKey;
	}

	public String getSoundKey() {
		return soundKey;
	}

	public static MoveType getMoveType(int move, boolean isCheckMove) {
		if (isCheckMove) {
			return CHECK;
		}
		if (Move.getPromotedPiece(move) != 0) {
			return PROMOTION;
		}
		if (Move.getCaptureFlag(move) != 0) {
			return CAPTURE;
		}
		if (Move.getCastleFlag(move) != 0) {
			return CASTLE;
		}

		return NORMAL;
	}

}
