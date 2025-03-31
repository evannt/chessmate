package game;

import chess.Position;
import engine.MoveGenerator;
import engine.MoveGenerator.MoveList;
import engine.Searcher;
import event.ChessEventManager;
import event.ChessEventType;
import event.ComputerMoveEvent;

public class ComputerPlayer implements Player {

	private Searcher searcher;
	private ChessEventManager chessEventManager;

	public ComputerPlayer() {
		searcher = new Searcher();
		chessEventManager = new ChessEventManager(ChessEventType.COMPUTER_MOVE);
	}

	public ChessEventManager getChessEventManager() {
		return chessEventManager;
	}

	@Override
	public boolean isHuman() {
		return false;
	}

	public synchronized void findMove(Position position) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);
		searcher.setPosition(position);
		int move = searcher.search(3);
		MoveType moveType = MoveType.getMoveType(move, position.isInCheck());
		System.out.println(moveType);
		ComputerMoveEvent computerMoveEvent = new ComputerMoveEvent(move, moveType, validMoves);
		chessEventManager.notify(computerMoveEvent);
	}

}
