package game;

import chess.Move;

public enum SoundType {

	NONE(""), NORMAL("move-self"), CAPTURE("capture"), CASTLE("castle"), PROMOTION("promote"), CHECK("move-check"),
	GAME_START("game-start"), GAME_END("game-end"), INVALID("notify");

	private String soundKey;

	private SoundType(String soundKey) {
		this.soundKey = soundKey;
	}

	public String getSoundKey() {
		return soundKey;
	}

	public static SoundType fromMove(int move, boolean isCheckMove) {
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
