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

	// Most Valuable Victim Least Valuable Attacker
	public static final int MVV_LVA[][] = { { 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000, 000 },
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
			{ 0, 100, 200, 300, 400, 500, 600, 100, 200, 300, 400, 500, 600 } };

	private Position pos;

	private int ply;
	private int nodes;

	private int killerMoves[][];
	private int historyMoves[][];

	private int pvLength[];
	private int pvTable[][];

	public Searcher(Position pos) {
		this.pos = pos;
		nodes = 0;
		killerMoves = new int[2][64];
		historyMoves = new int[PieceType.values().length + 1][64];
		pvLength = new int[MAX_PLY];
		pvTable = new int[MAX_PLY][MAX_PLY];
	}

	public void setPosition(Position pos) {
		this.pos = pos;
	}

	public void search(int depth) {
		iterativeDeepending(depth);
	}

	public void iterativeDeepending(int depth) {
		int score = 0;
		nodes = 0;

		Arrays.stream(killerMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(historyMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(pvTable).forEach(a -> Arrays.fill(a, 0));
		Arrays.fill(pvLength, 0);

		for (int d = 1; d <= depth; d++) {
			nodes = 0;
			score = negamax(-50000, 50000, d);
			System.out.println("score: " + score + " depth: " + d + " nodes: " + nodes);
			for (int c = 0; c < pvLength[0]; c++) {
				System.out.println(Move.decodeMove(pvTable[0][c]));
			}
		}
		System.out.println("Best move: " + Move.decodeMove(pvTable[0][0]));
		nodes = 0;

		Arrays.stream(killerMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(historyMoves).forEach(a -> Arrays.fill(a, 0));
		Arrays.stream(pvTable).forEach(a -> Arrays.fill(a, 0));
		Arrays.fill(pvLength, 0);

		score = negamax(-50000, 50000, depth);
		System.out.println("score: " + score + " depth: " + depth + " nodes: " + nodes);
		for (int c = 0; c < pvLength[0]; c++) {
			System.out.println(Move.decodeMove(pvTable[0][c]));
		}
		System.out.println("Best move: " + Move.decodeMove(pvTable[0][0]));
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
		boolean isKingInCheck = Bitboard.isSquareAttacked(
				pos.getTurn() == Piece.WHITE ? BitUtil.getLS1BIndex(pos.getBitboards()[PieceType.WKING.getKey()])
						: BitUtil.getLS1BIndex(pos.getBitboards()[PieceType.BKING.getKey()]),
				pos.getTurn() == Piece.WHITE ? Piece.BLACK : Piece.WHITE, pos.getBitboards(), pos.getOccupancies());

		if (isKingInCheck) {
			depth++;
		}

		int move;
		MoveList moveList = MoveGenerator.generateAllMoves(pos);
		sortMoves(moveList, pos);
		UndoInfo undoInfo = new UndoInfo();

		for (int c = 0; c < moveList.moveCount; c++) {
			move = moveList.mvs[c];
			if (!pos.makeMove(move, undoInfo, Position.ALL_MOVES)) {
				continue;
			}
			legalMoves++;

			ply++;
			int score = -negamax(-beta, -alpha, depth - 1);
			pos.unMakeMove(move, undoInfo);
			ply--;

			if (score >= beta) {
				if (Move.getCaptureFlag(move) == 0) {
					killerMoves[1][ply] = killerMoves[0][ply];
					killerMoves[0][ply] = move;
				}

				return beta; // move fails high
			}
			if (score > alpha) {
				if (Move.getCaptureFlag(move) == 0) {
					historyMoves[Move.getPiece(move)][Move.getDst(move)] += depth;
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

		return alpha; // move fails low
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

		int move;
		MoveList moveList = MoveGenerator.generateAllMoves(pos);
		sortMoves(moveList, pos);
		UndoInfo undoInfo = new UndoInfo();

		for (int c = 0; c < moveList.moveCount; c++) {
			move = moveList.mvs[c];
			if (!pos.makeMove(move, undoInfo, Position.CAPTURES)) {
				continue;
			}

			ply++;
			int score = -quiescence(-beta, -alpha);
			pos.unMakeMove(move, undoInfo);
			ply--;

			if (score >= beta) {
				return beta; // move fails high
			}
			if (score > alpha) {
				alpha = score;
			}
		}

		return alpha; // move fails low
	}

	public int scoreMove(int move, Position pos) {
		if (Move.getCaptureFlag(move) != 0) {
			int targetPiece = PieceType.WPAWN.getKey();

			int start = pos.getTurn() == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
			int end = pos.getTurn() == Piece.WHITE ? PieceType.BKING.getKey() : PieceType.WKING.getKey();
			for (int key = start; key <= end; key++) {
				if (BitUtil.getBit(pos.getBitboards()[key], Move.getDst(move)) == 1) {
					targetPiece = key;
					break;
				}
			}
			return MVV_LVA[Move.getPiece(move)][targetPiece] + 10000;
		} else {
			if (killerMoves[0][ply] == move) {
				return 9000;
			} else if (killerMoves[1][ply] == move) {
				return 8000;
			} else {
				return historyMoves[Move.getPiece(move)][Move.getDst(move)];
			}
		}
	}

	public void sortMoves(MoveList moveList, Position pos) {
		int[] scores = new int[moveList.moveCount];
		for (int i = 0; i < moveList.moveCount; i++) {
			scores[i] = scoreMove(0, pos);
		}

		for (int currMove = 0; currMove < moveList.moveCount; currMove++) {
			for (int nextMove = currMove + 1; nextMove < moveList.moveCount; nextMove++) {
				if (scores[currMove] < scores[nextMove]) {
					int tempScore = scores[currMove];
					scores[currMove] = scores[nextMove];
					scores[nextMove] = tempScore;

					int tempMove = moveList.mvs[currMove];
					moveList.mvs[currMove] = moveList.mvs[nextMove];
					moveList.mvs[nextMove] = tempMove;
				}
			}
		}
	}

}
