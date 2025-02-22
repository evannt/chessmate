package chess;

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

	private final int moveEncoding;

	private int score;

	public Move(int src, int dst, int piece, int promotedPiece, int captureFlag, int doublePawnPushFlag,
			int enPassantFlag, int castleFlag) {
		moveEncoding = (src) | (dst << 6) |
				(piece << 12) | (promotedPiece << 16) | (captureFlag << 20) |
				(doublePawnPushFlag << 21) | (enPassantFlag << 22) | (castleFlag << 23);
	}

//	public void scoreMove(Position pos) {
//		if (getCaptureFlag() != 0) {
//			int targetPiece = PieceType.WPAWN.getKey();
//
//			int start = pos.getTurn() == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
//			int end = pos.getTurn() == Piece.WHITE ? PieceType.BKING.getKey() : PieceType.WKING.getKey();
//			for (int key = start; key <= end; key++) {
//				if (BitUtil.getBit(pos.getBitboards()[key], getDst()) == 1) {
//					targetPiece = key;
//					break;
//				}
//			}
//			setScore(Searcher.MVV_LVA[getPiece()][targetPiece]);
////			return MVV_LVA[move.getPiece()][targetPiece];
//		}
//	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public int getMoveEncoding() {
		return moveEncoding;
	}

	public String decodeMove() {
		String src = BoardUtil.getIndexAsSquare(getSrc());
		String dst = BoardUtil.getIndexAsSquare(getDst());
		String promotedPiece = PieceType.getIdByKey(getPromotedPiece()).toLowerCase();
		return src + dst + (promotedPiece.equals(".") ? "" : promotedPiece);
	}

	public int getSrc() {
		return moveEncoding & SRC;
	}

	public int getDst() {
		return (moveEncoding & DST) >>> 6;
	}

	public int getPiece() {
		return (moveEncoding & PIECE) >>> 12;
	}

	public int getPromotedPiece() {
		return (moveEncoding & PROMOTED_PIECE) >>> 16;
	}

	public int getCaptureFlag() {
		return (moveEncoding & CAPTURE_FLAG);
	}

	public int getDoublePawnPushFlag() {
		return (moveEncoding & DOUBLE_PAWN_PUSH_FLAG);
	}

	public int getEnPassantFlag() {
		return (moveEncoding & EN_PASSANT_FLAG);
	}

	public int getCastleFlag() {
		return (moveEncoding & CASTLE_FLAG);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Move)) {
			return false;
		}
		Move move = (Move) o;
		return moveEncoding == move.moveEncoding;
	}

}
