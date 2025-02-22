package engine;

import java.util.Arrays;

import chess.Bitboard;
import chess.Move;
import chess.Piece;
import chess.PieceType;
import chess.Position;
import chess.UndoInfo;
import engine.MoveGenerator.MoveList;
import util.BitUtil;

public class Searcher {

	private static final int MAX_PLY = 64;

	public static final int MVV_LVA[][] = {
			{ 0, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000 },
			{ 0, 105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605 },
			{ 0, 104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604 },
			{ 0, 103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603 },
			{ 0, 102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602 },
			{ 0, 101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601 },
			{ 0, 100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600 },
			{ 0, 105, 205, 305, 405, 505, 605, 105, 205, 305, 405, 505, 605 },
			{ 0, 104, 204, 304, 404, 504, 604, 104, 204, 304, 404, 504, 604 },
			{ 0, 103, 203, 303, 403, 503, 603, 103, 203, 303, 403, 503, 603 },
			{ 0, 102, 202, 302, 402, 502, 602, 102, 202, 302, 402, 502, 602 },
			{ 0, 101, 201, 301, 401, 501, 601, 101, 201, 301, 401, 501, 601 },
			{ 0, 100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600 }
	};

	private Position pos;

	private int ply;
	private int nodes;

	private Move killerMoves[][];
	private int historyMoves[][];

	private int pvLength[];
	private Move pvTable[][];

	public Searcher(Position pos) {
		this.pos = pos;
		nodes = 0;
		killerMoves = new Move[2][64];
		historyMoves = new int[PieceType.values().length][64];
		pvLength = new int[MAX_PLY];
		pvTable = new Move[MAX_PLY][MAX_PLY];
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public void search(int depth) {
		iterativeDeepending(depth);
		int score = negamax(-50000, 50000, depth);
		System.out.println("Best move: " + pvTable[0][0].decodeMove() + " - " + score);
	}

	public void iterativeDeepending(int depth) {
		int score = 0;
		nodes = 0;

		Arrays.stream(killerMoves).forEach(a -> Arrays.fill(a, null));
		Arrays.stream(historyMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(pvTable).forEach(a -> Arrays.fill(a, null));
		Arrays.fill(pvLength, 0);

		for (int d = 1; d <= depth; d++) {
			nodes = 0;
			score = negamax(-50000, 50000, d);
			System.out.println("score: " + score + " depth: " + d + " nodes: " + nodes);
			for (int c = 0; c < pvLength[0]; c++) {
				System.out.println(pvTable[0][c].decodeMove());
			}
		}
		System.out.println("Best move: " + pvTable[0][0].decodeMove());
		nodes = 0;

		Arrays.stream(killerMoves).forEach(a -> Arrays.fill(a, null));
		Arrays.stream(historyMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(pvTable).forEach(a -> Arrays.fill(a, null));
		Arrays.fill(pvLength, 0);

		score = negamax(-50000, 50000, depth);
		System.out.println("score: " + score + " depth: " + depth + " nodes: " + nodes);
		for (int c = 0; c < pvLength[0]; c++) {
			System.out.println(pvTable[0][c].decodeMove());
		}
		System.out.println("Best move: " + pvTable[0][0].decodeMove());
	}

	public int negamax(int alpha, int beta, int depth) {
		pvLength[ply] = ply;
		if (depth == 0) {
			return quiescence(alpha, beta);
		}
		if (ply > MAX_PLY - 1) {
			return Evaluator.evaluate(pos);
		}

		nodes++;

		int legalMoves = 0;
		boolean isKingInCheck = Bitboard.isSquareAttacked(pos.getTurn() == Piece.WHITE ?
				BitUtil.getLS1BIndex(pos.getBitboards()[PieceType.WKING.getKey()]) :
				BitUtil.getLS1BIndex(pos.getBitboards()[PieceType.BKING.getKey()]),
				pos.getTurn() == Piece.WHITE ? Piece.BLACK : Piece.WHITE,
				pos.getBitboards(),
				pos.getOccupancies());

		if (isKingInCheck) {
			depth++;
		}

		Move move;
		MoveList moveList = MoveGenerator.generateAllMoves(pos);
//		moveList.sortMoves(pos);
		sortMoves(moveList, pos);
		UndoInfo undoInfo = new UndoInfo();

		for (int c = 0; c < moveList.moveCount; c++) {
			move = moveList.moves[c];
			if (!pos.makeMove(move, undoInfo, Position.ALL_MOVES)) {
//				pos.unMakeMove(undoInfo);
				continue;
			}
			legalMoves++;

			ply++;
			int score = -negamax(-beta, -alpha, depth - 1);
			pos.unMakeMove(undoInfo);
			ply--;

			if (score >= beta) {
				if (move.getCaptureFlag() == 0) {
					killerMoves[1][ply] = killerMoves[0][ply];
					killerMoves[0][ply] = move;
				}

				return beta; // move fails high
			}
			if (score > alpha) {
				if (move.getCaptureFlag() == 0) {
					historyMoves[move.getPiece()][move.getDst()] += depth;
				}
				alpha = score;
				pvTable[ply][ply] = move;
				for (int nextPly = ply + 1; nextPly < pvLength[ply + 1]; nextPly++) {
					pvTable[ply][nextPly] = pvTable[ply + 1][nextPly];
				}
				pvLength[ply] = pvLength[ply + 1];
			}
		}

		if (legalMoves == 0) {
			if (isKingInCheck) {
				return -49000 + ply;
			} else {
				return 0; // stalemate score
			}
		}

		return alpha;
	}

	public int quiescence(int alpha, int beta) {
		nodes++;
		int eval = Evaluator.evaluate(pos);

		if (eval >= beta) {
			return beta;
		}

		if (eval > alpha) {
			alpha = eval;
		}

		Move move;
		MoveList moveList = MoveGenerator.generateAllMoves(pos);
//		moveList.sortMoves(pos);
		sortMoves(moveList, pos);
		UndoInfo undoInfo = new UndoInfo();

		for (int c = 0; c < moveList.moveCount; c++) {
			move = moveList.moves[c];
			if (!pos.makeMove(move, undoInfo, Position.CAPTURES)) {
//				pos.unMakeMove(undoInfo);
				continue;
			}

			ply++;
			int score = -quiescence(-beta, -alpha);
			pos.unMakeMove(undoInfo);
			ply--;

			if (score >= beta) {
				return beta; // move fails high
			}
			if (score > alpha) {
				alpha = score;
			}
		}

		return alpha;
	}

	public void scoreMove(Move move, Position pos) {
		if (move.getCaptureFlag() != 0) {
			int targetPiece = PieceType.WPAWN.getKey();

			int start = pos.getTurn() == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
			int end = pos.getTurn() == Piece.WHITE ? PieceType.BKING.getKey() : PieceType.WKING.getKey();
			for (int key = start; key <= end; key++) {
				if (BitUtil.getBit(pos.getBitboards()[key], move.getDst()) == 1) {
					targetPiece = key;
					break;
				}
			}
			move.setScore(MVV_LVA[move.getPiece()][targetPiece] + 10000);
//			return MVV_LVA[move.getPiece()][targetPiece];
		} else {
			if (killerMoves[0][ply] != null && killerMoves[0][ply].equals(move)) {
				move.setScore(9000);
			} else if (killerMoves[1][ply] != null && killerMoves[1][ply].equals(move)) {
				move.setScore(8000);
			} else {
				move.setScore(historyMoves[move.getPiece()][move.getDst()]);
			}
		}
	}

	public void sortMoves(MoveList moveList, Position pos) {
		for (int i = 0; i < moveList.moveCount; i++) {
			scoreMove(moveList.moves[i], pos);
		}
//		Arrays.sort(moveList.moves, Comparator.nullsLast(Comparator.comparingInt(Move::getScore).reversed()));
		for (int currMove = 0; currMove < moveList.moveCount; currMove++) {
			for (int nextMove = currMove + 1; nextMove < moveList.moveCount; nextMove++) {
				if (moveList.moves[currMove].getScore() < moveList.moves[nextMove].getScore()) {
					Move tempMove = moveList.moves[currMove];
					moveList.moves[currMove] = moveList.moves[nextMove];
					moveList.moves[nextMove] = tempMove;
				}
			}
		}
	}

}
