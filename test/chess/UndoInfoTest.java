package chess;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import util.BitUtil;
import util.BoardUtil;

class UndoInfoTest {

	@Test
	public void testUnMakePawnMove() {
		Position position = new Position();
		position.setPosition(Position.START_POSITION);

		int e2 = BoardUtil.getSquareAsIndex("e2");
		int e4 = BoardUtil.getSquareAsIndex("e4");

		int move = Move.encodeMove(e2, e4, PieceType.WPAWN.getKey(), 0, 0, 0, 0, 0);
		int piece = Move.getPiece(move);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e2));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e4));

		position.unMakeMove(move, ui);

		assertEquals(Piece.WHITE, position.getTurn());

		assertEquals(0, position.getHalfMoveClock());
		assertEquals(1, position.getFullMoveCount());
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e2));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e4));
	}

	@Test
	public void testUnMakeCastleKingMove() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1");

		int e1 = BoardUtil.getSquareAsIndex("e1");
		int g1 = BoardUtil.getSquareAsIndex("g1");
		int h1 = BoardUtil.getSquareAsIndex("h1");
		int f1 = BoardUtil.getSquareAsIndex("f1");

		int move = Move.encodeMove(e1, g1, PieceType.WKING.getKey(), 0, 0, 0, 0, 1);
		int piece = Move.getPiece(move);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], g1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], h1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], f1));

		position.unMakeMove(move, ui);

		assertEquals(Piece.WHITE, position.getTurn());

		assertEquals(0, position.getHalfMoveClock());
		assertEquals(1, position.getFullMoveCount());
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], g1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], h1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], f1));
	}

	@Test
	public void testUnMakeCastleQueenMove() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");

		int e1 = BoardUtil.getSquareAsIndex("e1");
		int c1 = BoardUtil.getSquareAsIndex("c1");
		int a1 = BoardUtil.getSquareAsIndex("a1");
		int d1 = BoardUtil.getSquareAsIndex("d1");

		int move = Move.encodeMove(e1, c1, PieceType.WKING.getKey(), 0, 0, 0, 0, 1);
		int piece = Move.getPiece(move);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], c1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], a1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], d1));

		position.unMakeMove(move, ui);

		assertEquals(Piece.WHITE, position.getTurn());

		assertEquals(0, position.getHalfMoveClock());
		assertEquals(1, position.getFullMoveCount());
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], c1));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], a1));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.WROOK.getKey()], d1));
	}

	@Test
	public void testUnMakeEnPassantMove() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppp1ppp/8/8/4Pp2/8/PPPP1PPP/RBNQKBNR w KQkq f4 0 1");

		int e4 = BoardUtil.getSquareAsIndex("e4");
		int f5 = BoardUtil.getSquareAsIndex("f5");
		int f4 = BoardUtil.getSquareAsIndex("f4");

		int move = Move.encodeMove(e4, f5, PieceType.WPAWN.getKey(), 0, 1, 0, 1, 0);
		int piece = Move.getPiece(move);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e4));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], f5));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.BPAWN.getKey()], f4));

		position.unMakeMove(move, ui);

		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e4));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], f5));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.BPAWN.getKey()], f4));
	}

	@Test
	public void testUnMakePawnPromotiontMove() {
		Position position = new Position();
		position.setPosition("5pk1/4P3/8/8/8/8/8/4K3 w - - 0 1");

		int e7 = BoardUtil.getSquareAsIndex("e7");
		int f8 = BoardUtil.getSquareAsIndex("f8");

		int move = Move.encodeMove(e7, f8, PieceType.WPAWN.getKey(), PieceType.WQUEEN.getKey(), 1, 0, 0, 0);
		int piece = Move.getPiece(move);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], e7));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], f8));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.WQUEEN.getKey()], f8));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.BPAWN.getKey()], f8));

		position.unMakeMove(move, ui);

		assertEquals(1, BitUtil.getBit(position.getBitboards()[piece], e7));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[piece], f8));
		assertEquals(0, BitUtil.getBit(position.getBitboards()[PieceType.WQUEEN.getKey()], f8));
		assertEquals(1, BitUtil.getBit(position.getBitboards()[PieceType.BPAWN.getKey()], f8));
	}

}
