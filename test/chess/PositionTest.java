package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import util.BoardUtil;

class PositionTest {

	@Test
	public void testMoveGenMakeMove() {
		Position position = new Position();
		position.setPosition(Position.START_POSITION);

		int e2 = BoardUtil.getSquareAsIndex("e2");
		int e4 = BoardUtil.getSquareAsIndex("e4");
		int move = Move.encodeMove(e2, e4, PieceType.WPAWN.getKey(), 0, 0, 0, 0, 0);

		UndoInfo ui = new UndoInfo();
		position.makeMove(move, ui);

		assertEquals(Piece.BLACK, position.getTurn());

		assertEquals(0, position.getHalfMoveClock());
		assertEquals(1, position.getFullMoveCount());

	}

	@Test
	public void testUserMakeMove() {

	}

}
