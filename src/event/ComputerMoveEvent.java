package event;

import engine.MoveGenerator.MoveList;
import game.MoveType;

public class ComputerMoveEvent implements ChessEvent {

	private int move;
	private MoveType moveType;
	private MoveList validMoves;

	public ComputerMoveEvent(int move, MoveType moveType, MoveList validMoves) {
		this.move = move;
		this.moveType = moveType;
		this.validMoves = validMoves;
	}

	public int getMove() {
		return move;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public MoveList getValidMoves() {
		return validMoves;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.COMPUTER_MOVE;
	}

}
