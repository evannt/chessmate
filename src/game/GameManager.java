package game;

import java.awt.event.MouseEvent;

import chess.Move;
import chess.MoveLog;
import chess.Piece;
import chess.PieceType;
import chess.Position;
import chess.UndoInfo;
import engine.MoveGenerator;
import engine.MoveGenerator.MoveList;
import event.ChessEvent;
import event.ChessEventListener;
import event.ChessEventManager;
import event.ChessEventType;
import event.ComputerMoveEvent;
import event.GameResultEvent;
import event.PawnPromotionEvent;
import event.UpdateBoardEvent;
import gui.ChessBoardPainter;
import util.BoardUtil;

public class GameManager implements ChessEventListener {

	private GameState gameState;

	private GameMode gameMode;
	private Player whitePlayer;
	private Player blackPlayer;

	private Thread computerThread;

	private ChessEventManager chessEventManager;

	private MoveLog moveLog;

	private Position position;
	private int selectedSquare;
	private int activeSquare;

	public GameManager(GameMode gameMode, int selectedColor) {
		this.gameMode = gameMode;
		switch (gameMode) {
		case PLAY_BOT:
			if (selectedColor == Piece.WHITE) {
				whitePlayer = new HumanPlayer();
				blackPlayer = new ComputerPlayer(this);

			} else {
				whitePlayer = new ComputerPlayer(this);
				blackPlayer = new HumanPlayer();
			}
			break;
		case PLAY_FRIEND:
			whitePlayer = new HumanPlayer();
			blackPlayer = new HumanPlayer();
			break;
		}

		chessEventManager = new ChessEventManager(ChessEventType.values());
		moveLog = new MoveLog();
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
		gameState = GameState.ONGOING;
	}

	public void restartGame() {
		moveLog = new MoveLog();
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
		gameState = GameState.ONGOING;
	}

	public void addSubscriber(ChessEventListener listener, ChessEventType... operations) {
		chessEventManager.subscribe(listener, operations);
	}

	public boolean isGameOngoing() {
		return gameState == GameState.ONGOING;
	}

	public boolean isHumanTurn() {
		return position.getTurn() == Piece.WHITE ? whitePlayer.isHuman() : blackPlayer.isHuman();
	}

	private ComputerPlayer getComputerPlayer(int turn) {
		if (turn == Piece.WHITE) {
			return (ComputerPlayer) whitePlayer;
		}
		return (ComputerPlayer) blackPlayer;
	}

	public MoveLog getMoveLog() {
		return moveLog;
	}

	public Position getPosition() {
		return position;
	}

	public int getSelectedSquare() {
		return selectedSquare;
	}

	public void setSelectedSquare(int selectedSquare) {
		this.selectedSquare = selectedSquare;
	}

	public void setActiveSquare(int activeSquare) {
		this.activeSquare = activeSquare;
	}

	public ChessEventManager getChessEventManager() {
		return chessEventManager;
	}

	public boolean isCheckmate() {
		return position.isInCheck() &&
				MoveGenerator.generateAllMoves(position).removeIllegalMoves(position).moveCount == 0;
	}

	public boolean isStalemate() {
		return !position.isInCheck() &&
				MoveGenerator.generateAllMoves(position).removeIllegalMoves(position).moveCount == 0;
	}

	public void startComputerThinking() {
		ComputerPlayer computerPlayer = getComputerPlayer(position.getTurn());
		Runnable run = () -> {
			computerPlayer.findMove(new Position(position));

			stopComputerThinking();
		};
		computerThread = new Thread(run);
		computerThread.start();
	}

	public void stopComputerThinking() {
		if (computerThread != null) {
			try {
				computerThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			computerThread = null;
		}
	}

	public void mousePressed(MouseEvent e, int square) {
		if (selectedSquare == -1) { // Attempting to select a piece
			if (position.hasPiece(square)) {
				setSelectedSquare(square);
			}
		} else {
			if (position.hasPiece(square)) {
				setSelectedSquare(square);
				setActiveSquare(-1);
			}
		}
	}

	public void mouseReleased(MouseEvent e, int square) {
		if (selectedSquare == square) {
			if (activeSquare != square && activeSquare != -1) { // Clicking move
				movePiece(activeSquare, square);
				setSelectedSquare(-1);
				setActiveSquare(-1);
			} else {
				setActiveSquare(square);
				position.setPiecePosition(square, square);
			}
		} else if (selectedSquare != square && activeSquare != -1) { // Dragging move
			movePiece(selectedSquare, square);
			setSelectedSquare(-1);
			setActiveSquare(-1);
		}

		position.drawPieces();
		System.out.println(position.getFenString());
	}

	public void mouseDragged(MouseEvent e) {
		if (selectedSquare != -1) {
			setActiveSquare(selectedSquare);
			Piece p = position.getPiece(selectedSquare);
			if (p != null) {
				p.setX(e.getX() - (ChessBoardPainter.TILE_SIZE / 2));
				p.setY(e.getY() - (ChessBoardPainter.TILE_SIZE / 2));
			}
		}
	}

	private void movePiece(int from, int to) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);
		int fromRank = BoardUtil.getRankFromIndex(from);
		int fromFile = BoardUtil.getFileFromIndex(from);

		UndoInfo ui = new UndoInfo();
		int move = getMove(from, to, validMoves);
		if (move == 0) {
			// Reset location
			position.setPiecePosition(activeSquare, fromRank, fromFile);
			chessEventManager.notify(new UpdateBoardEvent(SoundType.INVALID));
		} else {
			if (Move.getPromotedPiece(move) != 0) {
				// Pawn Promotion
				position.setPiecePosition(activeSquare, fromRank, fromFile);
				PawnPromotionEvent promotionEvent = new PawnPromotionEvent(to, position.getTurn());
				chessEventManager.notify(promotionEvent);
				PieceType promotedPiece = promotionEvent.getPromotedPiece();
				move = Move.updatePromotionFlag(move, promotedPiece.getKey());
			}
			position.makeMove(move, ui);
			updateGameState();
			moveLog.addMove(position, validMoves, move, gameState == GameState.CHECKMATE);
			chessEventManager.notify(new UpdateBoardEvent(SoundType.fromMove(move, position.isInCheck())));
			checkGameState();

			if (gameState == GameState.ONGOING && !isHumanTurn()) {
				startComputerThinking();
			}
		}
	}

	public void resetActivePiecePosition() {
		position.setPiecePosition(activeSquare, BoardUtil.getRankFromIndex(activeSquare),
				BoardUtil.getFileFromIndex(activeSquare));
	}

	private int getMove(int src, int dst, MoveList validMoves) {
		for (int i = 0; i < validMoves.moveCount; i++) {
			int move = validMoves.mvs[i];
			if (Move.getSrc(move) == src && Move.getDst(move) == dst) {
				return move;
			}
		}

		return 0;
	}

	@Override
	public void update(ChessEvent event) {
		if (event instanceof ComputerMoveEvent computerMoveEvent) {
			UndoInfo ui = new UndoInfo();
			int move = computerMoveEvent.getMove();
			position.makeMove(move, ui);
			updateGameState();
			moveLog.addMove(position, computerMoveEvent.getValidMoves(), move, gameState == GameState.CHECKMATE);
			chessEventManager.notify(new UpdateBoardEvent(SoundType.fromMove(move, position.isInCheck())));
			checkGameState();
		}
	}

	private void checkGameState() {
		switch (gameState) {
		case CHECKMATE:
			int winner = position.getTurn() == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
			GameResultEvent checkmate = new GameResultEvent(gameState, gameMode, winner);
			chessEventManager.notify(checkmate);
			break;
		case STALEMATE:
			GameResultEvent stalemate = new GameResultEvent(gameState, gameMode, Piece.BOTH);
			chessEventManager.notify(stalemate);
			break;
		default:
			break;
		}
	}

	private void updateGameState() {
		if (isCheckmate()) {
			gameState = GameState.CHECKMATE;
		} else if (isStalemate()) {
			gameState = GameState.STALEMATE;
		}
	}

}
