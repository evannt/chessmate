package engine;

import chess.Piece;
import chess.PieceType;
import chess.Position;
import util.BitUtil;

public class Evaluator {

	private static int materialScore[] = {
			0, // empty square score
			100, // white pawn score
			300, // white knight score
			350, // white bishop score
			500, // white rook score
			1000, // white queen score
			10000, // white king score
			-100, // black pawn score
			-300, // black knight score
			-350, // black bishop score
			-500, // black rook score
			-1000, // black queen score
			-10000 // black king score
	};

	protected static final int PAWN_SCORE[] = {
		    90,  90,  90,  90,  90,  90,  90,  90,
		    30,  30,  30,  40,  40,  30,  30,  30,
		    20,  20,  20,  30,  30,  30,  20,  20,
		    10,  10,  10,  20,  20,  10,  10,  10,
		     5,   5,  10,  20,  20,   5,   5,   5,
		     0,   0,   0,   5,   5,   0,   0,   0,
		     0,   0,   0, -10, -10,   0,   0,   0,
		     0,   0,   0,   0,   0,   0,   0,   0
	};

	protected static final int KNIGHT_SCORE[] = {
		    -5,   0,   0,   0,   0,   0,   0,  -5,
		    -5,   0,   0,  10,  10,   0,   0,  -5,
		    -5,   5,  20,  20,  20,  20,   5,  -5,
		    -5,  10,  20,  30,  30,  20,  10,  -5,
		    -5,  10,  20,  30,  30,  20,  10,  -5,
		    -5,   5,  20,  10,  10,  20,   5,  -5,
		    -5,   0,   0,   0,   0,   0,   0,  -5,
		    -5, -10,   0,   0,   0,   0, -10,  -5
	};

	protected static final int BISHOP_SCORE[] = {
		     0,   0,   0,   0,   0,   0,   0,   0,
		     0,   0,   0,   0,   0,   0,   0,   0,
		     0,   0,   0,  10,  10,   0,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,  10,   0,   0,   0,   0,  10,   0,
		     0,  30,   0,   0,   0,   0,  30,   0,
		     0,   0, -10,   0,   0, -10,   0,   0
	};

	protected static final int ROOK_SCORE[] = {
		    50,  50,  50,  50,  50,  50,  50,  50,
		    50,  50,  50,  50,  50,  50,  50,  50,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,  10,  20,  20,  10,   0,   0,
		     0,   0,   0,  20,  20,   0,   0,   0
	};

	protected static final int QUEEN_SCORE[] = {
			-10, -5, 0, 0, 0, 0, -5, -10,
			-5, 0, 5, 5, 5, 5, 0, -5,
			0, 5, 5, 10, 10, 10, 10, 0,
			0, 5, 10, 10, 10, 10, 5, 0,
			0, 5, 10, 10, 10, 10, 5, 0,
			0, 5, 5, 10, 10, 5, 5, 0,
			-5, 0, 5, 5, 5, 5, 0, -5,
			-10, -5, 0, 0, 0, 0, -5, -10
	};

	protected static final int KING_SCORE[] = {
		     0,   0,   0,   0,   0,   0,   0,   0,
		     0,   0,   5,   5,   5,   5,   0,   0,
		     0,   5,   5,  10,  10,   5,   5,   0,
		     0,   5,  10,  20,  20,  10,   5,   0,
		     0,   5,  10,  20,  20,  10,   5,   0,
		     0,   0,   5,  10,  10,   5,   0,   0,
		     0,   5,   5,  -5,  -5,   0,   5,   0,
		     0,   0,   5,   0, -15,   0,  10,   0
	};

	protected static final int MIRROR_SCORE[] = {
			56, 57, 58, 59, 60, 61, 62, 63,
			48, 49, 50, 51, 52, 53, 54, 55,
			40, 41, 42, 43, 44, 45, 46, 47,
			32, 33, 34, 35, 36, 37, 38, 39,
			24, 25, 26, 27, 28, 29, 30, 31,
			16, 17, 18, 19, 20, 21, 22, 23,
			8, 9, 10, 11, 12, 13, 14, 15,
			0, 1, 2, 3, 4, 5, 6, 7
	};

	public static int evaluate(Position pos) {
		int score = 0, sq;
		long bitboard;

		for (int p = PieceType.WPAWN.getKey(); p <= PieceType.BKING.getKey(); p++) {
			bitboard = pos.getBitboards()[p];
			while (bitboard != 0) {
				sq = BitUtil.getLS1BIndex(bitboard);

				score += materialScore[p];

				switch (PieceType.valueOfKey(p)) {
				case WPAWN:
					score += PAWN_SCORE[sq];
					break;
				case WKNIGHT:
					score += KNIGHT_SCORE[sq];
					break;
				case WBISHOP:
					score += BISHOP_SCORE[sq];
					break;
				case WROOK:
					score += ROOK_SCORE[sq];
					break;
				case WQUEEN:
//					score += QUEEN_SCORE[sq];
					break;
				case WKING:
					score += KING_SCORE[sq];
					break;
				case BPAWN:
					score -= PAWN_SCORE[MIRROR_SCORE[sq]];
					break;
				case BKNIGHT:
					score -= KNIGHT_SCORE[MIRROR_SCORE[sq]];
					break;
				case BBISHOP:
					score -= BISHOP_SCORE[MIRROR_SCORE[sq]];
					break;
				case BROOK:
					score -= ROOK_SCORE[MIRROR_SCORE[sq]];
					break;
				case BQUEEN:
//					score -= QUEEN_SCORE[MIRROR_SCORE[sq]];
					break;
				case BKING:
					score -= KING_SCORE[MIRROR_SCORE[sq]];
					break;
				default:
					break;
				}

				bitboard = BitUtil.popBit(bitboard, sq);
			}
		}
		return pos.getTurn() == Piece.WHITE ? score : -score;
	}

}
