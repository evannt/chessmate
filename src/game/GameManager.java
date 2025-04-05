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
import event.PawnPromotionEvent;
import gui.ChessBoardPainter;
import util.BoardUtil;

// Receive notifications from both players and call the player do move method
public class GameManager implements ChessEventListener {

	// TODO Add support for player vs. player and player vs. computer
	private Player whitePlayer;
	private Player blackPlayer;

	private Thread computerThread;

	private ChessEventManager chessEventManager;

	private MoveLog moveLog;

	private Position position;
	private int selectedSquare;
	private int activeSquare;

	public GameManager(Player whitePlayer, Player blackPlayer) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		chessEventManager = new ChessEventManager(ChessEventType.PAWN_PROMOTION, ChessEventType.COMPUTER_MOVE);
		// listen to events from the computer
		if (this.whitePlayer instanceof ComputerPlayer whiteComputerPlayer) {
			whiteComputerPlayer.getChessEventManager().subscribe(this, ChessEventType.COMPUTER_MOVE);
		} else if (this.blackPlayer instanceof ComputerPlayer blackComputerPlayer) {
			blackComputerPlayer.getChessEventManager().subscribe(this, ChessEventType.COMPUTER_MOVE);
		}
		moveLog = new MoveLog();
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
	}

	public void addSubscriber(ChessEventListener listener, ChessEventType... operations) {
		chessEventManager.subscribe(listener, operations);
		if (this.whitePlayer instanceof ComputerPlayer whiteComputerPlayer) {
			whiteComputerPlayer.getChessEventManager().subscribe(listener, ChessEventType.COMPUTER_MOVE);
		} else if (this.blackPlayer instanceof ComputerPlayer blackComputerPlayer) {
			blackComputerPlayer.getChessEventManager().subscribe(listener, ChessEventType.COMPUTER_MOVE);
		}
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

	public MoveType mouseReleased(MouseEvent e, int square) {
		MoveType moveType = MoveType.NONE;
		if (selectedSquare == square) {
			if (activeSquare != square && activeSquare != -1) { // Clicking move
				moveType = movePiece(activeSquare, square);
				setSelectedSquare(-1);
				setActiveSquare(-1);
			} else {
				setActiveSquare(square);
				position.setPiecePosition(square, square);
			}
		} else if (selectedSquare != square && activeSquare != -1) { // Dragging move
			moveType = movePiece(selectedSquare, square);
			setSelectedSquare(-1);
			setActiveSquare(-1);
		}

//		position.drawPieces();
//		System.out.println(position.getFenString());
		return moveType;
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

	private MoveType movePiece(int from, int to) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);
		int fromRank = BoardUtil.getRankFromIndex(from);
		int fromFile = BoardUtil.getFileFromIndex(from);

		UndoInfo ui = new UndoInfo();
		int move = getMove(from, to, validMoves);
		if (move == 0) {
			// Reset location
			position.setPiecePosition(activeSquare, fromRank, fromFile);
			return MoveType.INVALID;
		}
		if (Move.getPromotedPiece(move) != 0) {
			// Pawn Promotion
			position.setPiecePosition(activeSquare, fromRank, fromFile);
			PawnPromotionEvent promotionEvent = new PawnPromotionEvent(to, position.getTurn());
			chessEventManager.notify(promotionEvent);
			PieceType promotedPiece = promotionEvent.getPromotedPiece();
			move = Move.updatePromotionFlag(move, promotedPiece.getKey());
		}
		// Move the piece
		position.makeMove(move, ui);
		moveLog.addMove(position, validMoves, move);

		if (!isHumanTurn()) {
			System.out.println("STARTING COMPUTER THINKING");
			startComputerThinking();
		}
		return MoveType.getMoveType(move, position.isInCheck()); // TODO Send to listeners
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
			moveLog.addMove(position, computerMoveEvent.getValidMoves(), move);
			position.makeMove(move, ui);
		}
	}

}
