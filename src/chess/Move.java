package chess;

import engine.Searcher;
import util.BitUtil;
import util.BoardUtil;

public class Move {

	private static final int SRC = 0x3f;
	private static final int DST = 0xfc0;
	private static final int PIECE = 0xf000;
	private static final int PROMOTED_PIECE = 0xf0000;
	private static final int CAPTURE_FLAG = 0x100000;
	private static final int DOUBLE_PAWN_PUSH_FLAG = 0x200000;
	private static final int EN_PASSANT_FLAG = 0x400000;
	private static final int CASTLE_FLAG = 0x800000;

	public static final int encodeMove(int src, int dst, int piece, int promotedPiece, int captureFlag,
			int doublePawnPushFlag, int enPassantFlag, int castleFlag) {
		return (src) | (dst << 6) | (piece << 12) | (promotedPiece << 16) | (captureFlag << 20)
				| (doublePawnPushFlag << 21) | (enPassantFlag << 22) | (castleFlag << 23);
	}

	public static final int updatePromotionFlag(int move, int promotedPiece) {
		return encodeMove(getSrc(move), getDst(move), getPiece(move), promotedPiece, getCaptureFlag(move),
				getDoublePawnPushFlag(move), getEnPassantFlag(move), getCastleFlag(move));
	}

	public static final int scoreMove(Position pos, int moveEncoding) {
		if (getCaptureFlag(moveEncoding) != 0) {
			int targetPiece = PieceType.WPAWN.getKey();

			int start = pos.getTurn() == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
			int end = pos.getTurn() == Piece.WHITE ? PieceType.BKING.getKey() : PieceType.WKING.getKey();
			for (int key = start; key <= end; key++) {
				if (BitUtil.getBit(pos.getBitboards()[key], getDst(moveEncoding)) == 1) {
					targetPiece = key;
					break;
				}
			}
			return Searcher.MVV_LVA[getPiece(moveEncoding)][targetPiece];
		}
		return 0;
	}

	public static final String decodeMove(int moveEncoding) {
		String src = BoardUtil.getIndexAsSquare(Move.getSrc(moveEncoding));
		String dst = BoardUtil.getIndexAsSquare(Move.getDst(moveEncoding));
		String promotedPiece = PieceType.getIdByKey(Move.getPromotedPiece(moveEncoding)).toLowerCase();
		return src + dst + (promotedPiece.equals(".") ? "" : promotedPiece);
	}

	public static final int getSrc(int moveEncoding) {
		return moveEncoding & SRC;
	}

	public static final int getDst(int moveEncoding) {
		return (moveEncoding & DST) >>> 6;
	}

	public static final int getPiece(int moveEncoding) {
		return (moveEncoding & PIECE) >>> 12;
	}

	public static final int getPromotedPiece(int moveEncoding) {
		return (moveEncoding & PROMOTED_PIECE) >>> 16;
	}

	public static final int getCaptureFlag(int moveEncoding) {
		return (moveEncoding & CAPTURE_FLAG);
	}

	public static final int getDoublePawnPushFlag(int moveEncoding) {
		return (moveEncoding & DOUBLE_PAWN_PUSH_FLAG);
	}

	public static final int getEnPassantFlag(int moveEncoding) {
		return (moveEncoding & EN_PASSANT_FLAG);
	}

	public static final int getCastleFlag(int moveEncoding) {
		return (moveEncoding & CASTLE_FLAG);
	}

}
