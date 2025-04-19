package event;

import game.SoundType;

public class UpdateBoardEvent implements ChessEvent {

	private SoundType soundType;

	public UpdateBoardEvent(SoundType soundType) {
		this.soundType = soundType;
	}

	public SoundType getSoundType() {
		return this.soundType;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.UPDATE_BOARD;
	}

}
