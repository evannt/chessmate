package engine;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import chess.Move;
import chess.Position;
import chess.UndoInfo;
import engine.MoveGenerator.MoveList;

class LegalMovesTest {

	private static Position position;
	private static final String PERFT_RESULTS_PATH = "test/perft-results/";

	private static HashMap<String, Integer> perft1Results;
	private static HashMap<String, Integer> perft2Results;
	private static HashMap<String, Integer> perft3Results;
	private static HashMap<String, Integer> perft4Results;
	private static HashMap<String, Integer> perft5Results;
	private static HashMap<String, Integer> perft6Results;

	@BeforeAll
	public static void setup() throws IOException {
		position = new Position();
//		position.setPosition(Position.START_POSITION);
//		position.setPosition(Position.POSITION_2);
//		position.setPosition("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
//		position.setPosition("rnbqkbnr/ppp1pppp/3p4/8/8/P7/1PPPPPPP/RNBQKBNR w KQkq - 0 1");
//		position.setPosition("rnbqkbnr/ppp1pppp/3p4/8/P7/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
//		position.setPosition("rnbqkbnr/1pp1pppp/p2p4/8/P7/8/1PPPPPPP/RNBQKBNR w KQkq - 0 1");
//		position.setPosition("rnbqkbnr/1pp1pppp/p2p4/P7/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
//		position.setPosition("rnbqkbnr/2p1pppp/pp1p4/P7/8/8/1PPPPPPP/RNBQKBNR w KQkq - 0 1");
//		position.setPosition("rnbqkbnr/2p1pppp/pP1p4/8/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
//		position.setPosition("rnbqkbnr/4pppp/pPpp4/8/8/8/1PPPPPPP/RNBQKBNR w KQkq - 0 1");
//		position.setPosition("rnbqkbnr/1P2pppp/p1pp4/8/8/8/1PPPPPPP/RNBQKBNR b KQkq - 0 1");
//		position.setPosition("rnbqkbnr/1P3ppp/p1ppp3/8/8/8/1PPPPPPP/RNBQKBNR w KQkq - 0 1");
//		position.setPosition("Qnbqkbnr/5ppp/p1ppp3/8/8/8/1PPPPPPP/RNBQKBNR b KQk - 0 1");
//		position.setPosition("Qnbqkbnr/6pp/p1pppp2/8/8/8/1PPPPPPP/RNBQKBNR w KQk - 0 1");
//		position.setPosition("Qnbqkbnr/6pp/p1pppp2/8/8/1P6/2PPPPPP/RNBQKBNR b KQk - 0 1");
//		position.setPosition("Qnb1kbnr/6pp/p1pppp2/q7/8/1P6/2PPPPPP/RNBQKBNR w KQk - 0 1");
//		position.setPosition("Qnb1kbnr/6pp/p1pppp2/q7/1P6/8/2PPPPPP/RNBQKBNR b KQk - 0 1");
//		position.setPosition("Qnb1kbnr/7p/p1ppppp1/q7/1P6/8/2PPPPPP/RNBQKBNR w KQk - 0 1");
		position.setPosition("Qnb1kbnr/7p/p1ppppp1/qP6/8/8/2PPPPPP/RNBQKBNR b KQk - 0 1");
		perft1Results = loadPerftResults(1);
		perft2Results = loadPerftResults(2);
		perft3Results = loadPerftResults(3);
		perft4Results = loadPerftResults(4);
		perft5Results = loadPerftResults(5);
		perft6Results = loadPerftResults(6);
	}

	@Test
	public void testPerft() {
//		doPerft(position, 6, new long[] { 20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L });
		debugPerft(1);
//		position.setPosition("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");
//		doPerft(position, 5, new long[] { 14, 191, 2812, 43238, 674624, 11030083, 178633661 });

//        game.processString("setpos r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
//        doTestPerfT(game.pos, 4, new long[]{48,2039,97862,4085603,193690690});
	}

//	private void doPerft(Position pos, int maxDepth, long[] expectedNodeCounts) {
//		for (int d = 1; d <= maxDepth; d++) {
//			long t0 = System.nanoTime();
//			long nodes = perft(pos, d);
//			long t1 = System.nanoTime();
//			System.out.printf("perft(%d) = %d, t=%.6fs\n", d, nodes, (t1 - t0) * 1e-9);
//			assertEquals(expectedNodeCounts[d - 1], nodes);
//		}
//	}

//	private final static long perft(Position pos, int depth) {
//		if (depth == 0)
//			return 1;
//		long nodes = 0;
//		MoveList moves = MoveGenerator.generateAllMoves(pos);
//		if (depth == 1) {
//			return moves.moveCount;
//		}
//		UndoInfo ui = new UndoInfo();
//		for (int mi = 0; mi < moves.moveCount; mi++) {
//			Move move = moves.moves[mi];
//			if (!pos.makeMove(move, ui)) {
//				continue;
//			}
//			nodes += perft(pos, depth - 1);
//			pos.unMakeMove(ui);
//		}
//		return nodes;
//	}

	private static final long debugPerft(int depth) {
		return debugPerftAuxiliary(depth, depth);
	}

	private static final long debugPerftAuxiliary(int depth, int currentDepth) {
		if (currentDepth == 0) {
			return 1;
		}
		MoveList moves = MoveGenerator.generateAllMoves(position);
//		if (currentDepth == 1) {
//			return moves.moveCount;
//		}
		long nodes = 0;
		long currNodes = 0;
		UndoInfo ui = new UndoInfo();
		for (int mi = 0; mi < moves.moveCount; mi++) {
			Move move = moves.moves[mi];
			if (!position.makeMove(move, ui)) {
				continue;
			}
			currNodes = debugPerftAuxiliary(depth, currentDepth - 1);
			nodes += currNodes;
			String moveString = move.decodeMove();
			String errorMessage = moveString;
			System.out.println(moveString + ": " + currNodes);
			if (depth == currentDepth) {
				System.out.println(position.getFenString());
				switch (depth) {
				case 1:
					errorMessage += " Expected " + perft1Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft1Results.get(moveString) == currNodes);
					break;
				case 2:
					errorMessage += " Expected " + perft2Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft2Results.get(moveString) == currNodes);
					break;
				case 3:
					errorMessage += " Expected " + perft3Results.get(moveString) + " nodes but was " + currNodes;
					assertTrue(errorMessage, perft3Results.get(moveString) == currNodes);
					break;
				case 4:
					errorMessage += " Expected " + perft4Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft4Results.get(moveString) == currNodes);
					break;
				case 5:
					errorMessage += " Expected " + perft5Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft5Results.get(moveString) == currNodes);
					break;
				case 6:
					errorMessage += " Expected " + perft6Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft6Results.get(moveString) == currNodes);
					break;
				}
			}
			position.unMakeMove(ui);
		}
		return nodes;
	}

	private static final HashMap<String, Integer> loadPerftResults(int perftNum) throws IOException {
		HashMap<String, Integer> perftResults = new HashMap<>();
		String fileName = PERFT_RESULTS_PATH + "perft-" + perftNum + "-results.txt";
		List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
		for (String line : lines) {
			if (line.isBlank()) {
				continue;
			}
			String[] mapVals = line.split(": ");
			perftResults.put(mapVals[0], Integer.valueOf(mapVals[1]));
		}
		return perftResults;
	}

}
