package chess;

import java.awt.event.MouseEvent;

import engine.MoveGenerator;
import engine.MoveGenerator.MoveList;
import gui.ChessBoardPainter;
import util.BoardUtil;

public class GameManager {

	// TODO Add support for player vs. player and player vs. computer

	private int playerColor;

	private GameState gameState;

	private Position position;
	private int selectedSquare;
	private int activeSquare;
	private int promotionSrc;
	private int promotionDst;
	private int promotedPiece;

	public GameManager(int playerColor) {
		this.playerColor = playerColor;
		gameState = GameState.MENU;
		position = new Position();
		position.setPosition(Position.START_POSITION);
		selectedSquare = -1;
		activeSquare = -1;
		promotionSrc = -1;
		promotionDst = -1;
		promotedPiece = PieceType.NONE.getKey();
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

	public void mouseReleased(MouseEvent e, int square) {
		if (gameState == GameState.PAWN_PROMOTION) {
			promotePiece(square);
		} else if (selectedSquare == square) {
			if (activeSquare != square && activeSquare != -1) { // Clicking move
				movePiece(activeSquare, square);
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
				movePiece(selectedSquare, square);
				setSelectedSquare(-1);
				setActiveSquare(-1);
			}
		}

		position.drawPieces();
		System.out.println(position.getFenString());
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

	private void movePiece(int from, int to) {
		int fromRank = BoardUtil.getRankFromIndex(from);
		int fromFile = BoardUtil.getFileFromIndex(from);
		int toRank = BoardUtil.getRankFromIndex(to);
		int toFile = BoardUtil.getFileFromIndex(to);
		System.out.println("Moving: (" + fromRank + "," + fromFile + ") to (" + toRank + "," + toFile + ")");
		UndoInfo ui = new UndoInfo();
		int move = getMove(from, to);
		if (move != 0) {
			// Move the piece
			position.makeMove(move, ui);
		} else {
			// Reset location
			position.setPiecePosition(activeSquare, fromRank, fromFile);
		}

	}

	private void promotePiece(int selection) {
		int promotionFile = BoardUtil.getFileFromIndex(promotionSrc);
		int rank = BoardUtil.getRankFromIndex(selection);
		int file = BoardUtil.getFileFromIndex(selection);
		if (file != promotionFile) {
			setGameState(GameState.HUMAN_TURN);
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
			} else {
				UndoInfo ui = new UndoInfo();

				Piece p = position.getPiece(promotionSrc);
				int move = getPromotion(promotionSrc, promotionDst, p, choice);
				position.makeMove(move, ui);
				setGameState(GameState.COMPUTER_TURN);
			}
			setPromotionSrc(-1);
			setPromotionDst(-1);
		}
	}

	public void resetActivePiecePosition() {
		position.setPiecePosition(activeSquare, BoardUtil.getRankFromIndex(activeSquare),
				BoardUtil.getFileFromIndex(activeSquare));
	}

	private int getMove(int src, int dst) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);

		for (int i = 0; i < validMoves.moveCount; i++) {
			int move = validMoves.mvs[i];
			System.out.println(Move.decodeMove(move));
			if (Move.getSrc(move) == src && Move.getDst(move) == dst) {
				if (Move.getPromotedPiece(move) == PieceType.NONE.getKey()) {
					return move;
				} else {
					setGameState(GameState.PAWN_PROMOTION);
					setSelectedSquare(-1);
					setActiveSquare(-1);
					setPromotionSrc(src);
					setPromotionDst(dst);
					return 0;
				}
			}
		}

		return 0;
	}

	private int getPromotion(int src, int dst, Piece piece, int promotedPiece) {
		MoveList validMoves = MoveGenerator.generateAllMoves(position).removeIllegalMoves(position);

		for (int i = 0; i < validMoves.moveCount; i++) {
			int move = validMoves.mvs[i];
			System.out.println(Move.decodeMove(move));
			if (Move.getSrc(move) == src && Move.getDst(move) == dst && Move.getPromotedPiece(move) == promotedPiece) {
				setGameState(GameState.PAWN_PROMOTION);
				return move;
			}
		}

		return 0;
	}

}
