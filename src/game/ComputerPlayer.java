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

	public ComputerPlayer(GameManager gameManager) {
		searcher = new Searcher();
		chessEventManager = new ChessEventManager(ChessEventType.COMPUTER_MOVE);
		chessEventManager.subscribe(gameManager, ChessEventType.COMPUTER_MOVE);
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
		int move = searcher.search(3); // Optimize before increasing depth
		ComputerMoveEvent computerMoveEvent = new ComputerMoveEvent(move, validMoves);
		chessEventManager.notify(computerMoveEvent);
	}

}
