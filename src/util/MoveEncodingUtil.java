package util;

import chess.PieceType;

@Deprecated
public class MoveEncodingUtil {

	// TODO add nibbles representation

//	private static final int SRC = 0x3f;
//	private static final int DST = 0xfc0;
//	private static final int PIECE = 0xf000;
//	private static final int PROMOTED_PIECE = 0xf0000;
//	private static final int CAPTURE_FLAG = 0x100000;
//	private static final int DOUBLE_PAWN_PUSH_FLAG = 0x200000;
//	private static final int EN_PASSANT_FLAG = 0x400000;
//	private static final int CASTLE_FLAG = 0x800000;

	public static int encodeMove(int src, int dst, int piece, int promotedPiece, int captureFlag, int doublePawnPushFlag,
			int enPassantFlag, int castleFlag) {
		return (src) | (dst << 6) |
				(piece << 12) | (promotedPiece << 16) | (captureFlag << 20) |
				(doublePawnPushFlag << 21) | (enPassantFlag << 22) | (castleFlag << 23);
	}

	public static String decodeMove(int move) {
		String src = BoardUtil.getIndexAsSquare(getSrc(move));
		String dst = BoardUtil.getIndexAsSquare(getDst(move));
		String promotedPiece = PieceType.getIdByKey(getPromotedPiece(move));
		return src + dst + (promotedPiece.equals(".") ? "" : promotedPiece);
	}

	public static int getSrc(int move) {
		return move & 0x3f;
	}

	public static int getDst(int move) {
		return (move & 0xfc0) >>> 6;
	}

	public static int getPiece(int move) {
		return (move & 0xf000) >>> 12;
	}

	public static int getPromotedPiece(int move) {
		return (move & 0xf0000) >>> 16;
	}

	public static int getCaptureFlag(int move) {
		return (move & 0x100000);// >>> 20;
	}

	public static int getDoublePawnPushFlag(int move) {
		return (move & 0x200000);// >>> 21;
	}

	public static int getEnPassantFlag(int move) {
		return (move & 0x400000);// >>> 22;
	}

	public static int getCastleFlag(int move) {
		return (move & 0x800000);// >>> 23;
	}

}
