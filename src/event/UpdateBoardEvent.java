package event;

import game.SoundType;

public class UpdateBoardEvent implements ChessEvent {

	private SoundType moveType;

	public UpdateBoardEvent(SoundType moveType) {
		this.moveType = moveType;
	}

	public SoundType getMoveType() {
		return this.moveType;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.UPDATE_BOARD;
	}

}
