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
import event.ChessEventManager;
import event.ChessEventType;
import event.PawnPromotionEvent;
import gui.ChessBoardPainter;
import util.BoardUtil;

public class GameManager {

	// TODO Add support for player vs. player and player vs. computer
	private ChessEventManager chessEventManager;

	private int playerColor;

	private MoveLog moveLog;

	private Position position;
	private int selectedSquare;
	private int activeSquare;

	public GameManager(GameMode gameMode, int playerColor) {
		this.playerColor = Piece.WHITE;
		chessEventManager = new ChessEventManager(ChessEventType.PAWN_PROMOTION);
		moveLog = new MoveLog();
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
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

	public void mousePressed(MouseEvent e, int square) {
		if (selectedSquare == -1) { // Attempting to select a piece
			if (position.hasPiece(square)) {
				setSelectedSquare(square);
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
			if (position.hasPiece(square)) {
				setSelectedSquare(-1);
				setActiveSquare(-1);
			} else {
				moveType = movePiece(selectedSquare, square);
				setSelectedSquare(-1);
				setActiveSquare(-1);
			}
		}

		position.drawPieces();
		System.out.println(position.getFenString());
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
			PawnPromotionEvent promotionEvent = new PawnPromotionEvent(to, playerColor);
			chessEventManager.notify(ChessEventType.PAWN_PROMOTION, promotionEvent);
			PieceType promotedPiece = promotionEvent.getPromotedPiece();
			move = Move.encodeNewPromotion(move, promotedPiece.getKey());
		}
		// Move the piece
		position.makeMove(move, ui);
		moveLog.addMove(position, validMoves, move);
		return MoveType.getMoveType(move, position.isInCheck());
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

}
