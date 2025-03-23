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
import gui.ChessBoardPainter;
import util.BoardUtil;

public class GameManager {

	// TODO Add support for player vs. player and player vs. computer

	private int playerColor;

	private MoveLog moveLog;

	private GameState gameState;

	private Position position;
	private int selectedSquare;
	private int activeSquare;
	private int promotionSrc;
	private int promotionDst;
	private int promotedPiece;

	public GameManager(GameMode gameMode, int playerColor) {
		this.playerColor = Piece.WHITE;
		moveLog = new MoveLog();
		gameState = GameState.MENU;
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
		promotionSrc = -1;
		promotionDst = -1;
		promotedPiece = PieceType.NONE.getKey();
	}

	public MoveLog getMoveLog() {
		return moveLog;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
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

	public int getPromotionSrc() {
		return promotionSrc;
	}

	public void setPromotionSrc(int promotionSrc) {
		this.promotionSrc = promotionSrc;
	}

	public int getPromotionDst() {
		return promotionDst;
	}

	public void setPromotionDst(int promotionDst) {
		this.promotionDst = promotionDst;
	}

	public int getPromotedPiece() {
		return promotedPiece;
	}

	public void setPromotedPiece(int promotedPiece) {
		this.promotedPiece = promotedPiece;
	}

	public void mousePressed(MouseEvent e, int square) {
		if (selectedSquare == -1 && gameState != GameState.PAWN_PROMOTION) { // Attempting to select a piece
			if (position.hasPiece(square)) {
				setSelectedSquare(square);
			}
		}
	}

	public MoveType mouseReleased(MouseEvent e, int square) {
		MoveType moveType = MoveType.NONE;
		if (gameState == GameState.PAWN_PROMOTION) {
			moveType = promotePiece(square);
		} else if (selectedSquare == square) {
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
		if (selectedSquare != -1 && gameState != GameState.PAWN_PROMOTION) {
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
		int toRank = BoardUtil.getRankFromIndex(to);
		int toFile = BoardUtil.getFileFromIndex(to);
		System.out.println("Moving: (" + fromRank + "," + fromFile + ") to (" + toRank + "," + toFile + ")");
		UndoInfo ui = new UndoInfo();
		int move = getMove(from, to, validMoves);
		if (move == 0) {
			// Reset location
			position.setPiecePosition(activeSquare, fromRank, fromFile);
			return MoveType.INVALID;
		}
		if (Move.getPromotedPiece(move) != 0) {
			position.setPiecePosition(activeSquare, fromRank, fromFile);
			return MoveType.NONE;
		}
		// Move the piece
		position.makeMove(move, ui);
		moveLog.addMove(position, validMoves, move);
		return MoveType.getMoveType(move, position.isInCheck());
	}

	private MoveType promotePiece(int selection) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);
		int promotionFile = BoardUtil.getFileFromIndex(promotionSrc);
		int rank = BoardUtil.getRankFromIndex(selection);
		int file = BoardUtil.getFileFromIndex(selection);
		if (file != promotionFile) {
			setGameState(GameState.HUMAN_TURN);
			return MoveType.NONE;
		} else {
			int choice = switch (rank) {
			case ChessBoardPainter.QUEEN_PROMOTION ->
				playerColor == Piece.WHITE ? PieceType.WQUEEN.getKey() : PieceType.BQUEEN.getKey();
			case ChessBoardPainter.KNIGHT_PROMOTION ->
				playerColor == Piece.WHITE ? PieceType.WKNIGHT.getKey() : PieceType.BKNIGHT.getKey();
			case ChessBoardPainter.ROOK_PROMOTION ->
				playerColor == Piece.WHITE ? PieceType.WROOK.getKey() : PieceType.BROOK.getKey();
			case ChessBoardPainter.BISHOP_PROMOTION ->
				playerColor == Piece.WHITE ? PieceType.WBISHOP.getKey() : PieceType.BBISHOP.getKey();
			default -> PieceType.NONE.getKey();
			};
			if (choice == PieceType.NONE.getKey()) {
				setGameState(GameState.HUMAN_TURN);
				setPromotionSrc(-1);
				setPromotionDst(-1);
				return MoveType.NONE;
			}
			UndoInfo ui = new UndoInfo();
			Piece p = position.getPiece(promotionSrc);
			int move = getPromotion(promotionSrc, promotionDst, validMoves, p, choice);
			position.makeMove(move, ui);
			moveLog.addMove(position, validMoves, move);
			setGameState(GameState.COMPUTER_TURN);
			setPromotionSrc(-1);
			setPromotionDst(-1);
			return MoveType.getMoveType(move, position.isInCheck());
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
				if (Move.getPromotedPiece(move) == PieceType.NONE.getKey()) {
					return move;
				}
				setGameState(GameState.PAWN_PROMOTION);
				setSelectedSquare(-1);
				setActiveSquare(-1);
				setPromotionSrc(src);
				setPromotionDst(dst);
				return move;
			}
		}

		return 0;
	}

	private int getPromotion(int src, int dst, MoveList validMoves, Piece piece, int promotedPiece) {

		for (int i = 0; i < validMoves.moveCount; i++) {
			int move = validMoves.mvs[i];
			if (Move.getSrc(move) == src && Move.getDst(move) == dst && Move.getPromotedPiece(move) == promotedPiece) {
				setGameState(GameState.PAWN_PROMOTION);
				return move;
			}
		}

		return 0;
	}

}
