package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import util.BoardUtil;

public class MoveTest {

	@Test
	public void testPawnPromotionMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("a7");
		int dst = BoardUtil.getSquareAsIndex("b8");
		int piece = PieceType.WPAWN.getKey();
		int promotedPiece = PieceType.WQUEEN.getKey();
		int m = Move.encodeMove(src, dst, piece, promotedPiece, 1, 0, 0, 0);

		assertEquals(Move.getSrc(m), src);
		assertEquals(Move.getDst(m), dst);
		assertEquals(Move.getPiece(m), piece);
		assertEquals(Move.getPromotedPiece(m), promotedPiece);
		assertNotEquals(Move.getCaptureFlag(m), 0);
		assertEquals(Move.getDoublePawnPushFlag(m), 0);
		assertEquals(Move.getEnPassantFlag(m), 0);
		assertEquals(Move.getCastleFlag(m), 0);
	}

	@Test
	public void testDoublePawnPushMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("e2");
		int dst = BoardUtil.getSquareAsIndex("e4");
		int piece = PieceType.WPAWN.getKey();
		int m = Move.encodeMove(src, dst, piece, 0, 0, 1, 0, 0);

		assertEquals(Move.getSrc(m), src);
		assertEquals(Move.getDst(m), dst);
		assertEquals(Move.getPiece(m), piece);
		assertEquals(Move.getPromotedPiece(m), 0);
		assertEquals(Move.getCaptureFlag(m), 0);
		assertNotEquals(Move.getDoublePawnPushFlag(m), 0);
		assertEquals(Move.getEnPassantFlag(m), 0);
		assertEquals(Move.getCastleFlag(m), 0);
	}

	@Test
	public void testEnPassantMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("d5");
		int dst = BoardUtil.getSquareAsIndex("e6");
		int piece = PieceType.WPAWN.getKey();
		int m = Move.encodeMove(src, dst, piece, 0, 1, 0, 1, 0);

		assertEquals(Move.getSrc(m), src);
		assertEquals(Move.getDst(m), dst);
		assertEquals(Move.getPiece(m), piece);
		assertEquals(Move.getPromotedPiece(m), 0);
		assertNotEquals(Move.getCaptureFlag(m), 0);
		assertEquals(Move.getDoublePawnPushFlag(m), 0);
		assertNotEquals(Move.getEnPassantFlag(m), 0);
		assertEquals(Move.getCastleFlag(m), 0);
	}

	@Test
	public void testCastleMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("e8");
		int dst = BoardUtil.getSquareAsIndex("g8");
		int piece = PieceType.BKING.getKey();
		int m = Move.encodeMove(src, dst, piece, 0, 0, 0, 0, 1);

		assertEquals(Move.getSrc(m), src);
		assertEquals(Move.getDst(m), dst);
		assertEquals(Move.getPiece(m), piece);
		assertEquals(Move.getPromotedPiece(m), 0);
		assertEquals(Move.getCaptureFlag(m), 0);
		assertEquals(Move.getDoublePawnPushFlag(m), 0);
		assertEquals(Move.getEnPassantFlag(m), 0);
		assertNotEquals(Move.getCastleFlag(m), 0);
	}
}
