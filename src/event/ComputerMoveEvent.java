package event;

import engine.MoveGenerator.MoveList;

public class ComputerMoveEvent implements ChessEvent {

	private int move;
	private MoveList validMoves;

	public ComputerMoveEvent(int move, MoveList validMoves) {
		this.move = move;
		this.validMoves = validMoves;
	}

	public int getMove() {
		return move;
	}

	public MoveList getValidMoves() {
		return validMoves;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.COMPUTER_MOVE;
	}

}
