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
		Move m = new Move(src, dst, piece, promotedPiece, 1, 0, 0, 0);

		assertEquals(m.getSrc(), src);
		assertEquals(m.getDst(), dst);
		assertEquals(m.getPiece(), piece);
		assertEquals(m.getPromotedPiece(), promotedPiece);
		assertNotEquals(m.getCaptureFlag(), 0);
		assertEquals(m.getDoublePawnPushFlag(), 0);
		assertEquals(m.getEnPassantFlag(), 0);
		assertEquals(m.getCastleFlag(), 0);
	}

	@Test
	public void testDoublePawnPushMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("e2");
		int dst = BoardUtil.getSquareAsIndex("e4");
		int piece = PieceType.WPAWN.getKey();
		Move m = new Move(src, dst, piece, 0, 0, 1, 0, 0);

		assertEquals(m.getSrc(), src);
		assertEquals(m.getDst(), dst);
		assertEquals(m.getPiece(), piece);
		assertEquals(m.getPromotedPiece(), 0);
		assertEquals(m.getCaptureFlag(), 0);
		assertNotEquals(m.getDoublePawnPushFlag(), 0);
		assertEquals(m.getEnPassantFlag(), 0);
		assertEquals(m.getCastleFlag(), 0);
	}

	@Test
	public void testEnPassantMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("d5");
		int dst = BoardUtil.getSquareAsIndex("e6");
		int piece = PieceType.WPAWN.getKey();
		Move m = new Move(src, dst, piece, 0, 1, 0, 1, 0);

		assertEquals(m.getSrc(), src);
		assertEquals(m.getDst(), dst);
		assertEquals(m.getPiece(), piece);
		assertEquals(m.getPromotedPiece(), 0);
		assertNotEquals(m.getCaptureFlag(), 0);
		assertEquals(m.getDoublePawnPushFlag(), 0);
		assertNotEquals(m.getEnPassantFlag(), 0);
		assertEquals(m.getCastleFlag(), 0);
	}

	@Test
	public void testCastleMoveEncoding() {
		int src = BoardUtil.getSquareAsIndex("e8");
		int dst = BoardUtil.getSquareAsIndex("g8");
		int piece = PieceType.BKING.getKey();
		Move m = new Move(src, dst, piece, 0, 0, 0, 0, 1);

		assertEquals(m.getSrc(), src);
		assertEquals(m.getDst(), dst);
		assertEquals(m.getPiece(), piece);
		assertEquals(m.getPromotedPiece(), 0);
		assertEquals(m.getCaptureFlag(), 0);
		assertEquals(m.getDoublePawnPushFlag(), 0);
		assertEquals(m.getEnPassantFlag(), 0);
		assertNotEquals(m.getCastleFlag(), 0);
	}
}
