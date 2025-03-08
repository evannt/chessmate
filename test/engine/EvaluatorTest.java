package engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import chess.Position;
import util.BoardUtil;

class EvaluatorTest {
	
	private static final String SQUARES[] = {
			"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
			"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
			"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
			"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
			"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
			"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
			"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
			"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
	};
	
	@Test
	public void testMirrorScore() {
		for (int i = 0; i < SQUARES.length; i++) {
			assertEquals(BoardUtil.getSquareAsIndex(SQUARES[i]), Evaluator.MIRROR_SCORE[i]);
		}
	}
	
	@Test
	public void testEvaluateStartPosition() {
		Position position = new Position();
		position.setPosition(Position.START_POSITION);
		assertEquals(Evaluator.evaluate(position), 0);
	}
	
	@Test
	public void testEvaluateCustomPosition1() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/p1pppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(100, Evaluator.evaluate(position));
	}
	
	@Test
	public void testEvaluateCustomPosition2() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/3PPPPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(-300, Evaluator.evaluate(position));
	}
	
	@Test
	public void testEvaluateCustomPosition3() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/4P3/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(30, Evaluator.evaluate(position));
	}
	
	@Test
	public void testEvaluateCustomPosition4() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
		assertEquals(0, Evaluator.evaluate(position));
	}

	@Test
	public void testEvaluateCustomPosition5() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR w KQkq - 0 1");
		assertEquals(-500, Evaluator.evaluate(position));
	}
	
	@Test
	public void testEvaluateCustomPosition6() {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNB1KBNR w KQkq - 0 1");
		assertEquals(-1000, Evaluator.evaluate(position));
	}
	
}
