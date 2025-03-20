package engine;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

	private static HashMap<String, Long> perft1Results;
	private static HashMap<String, Long> perft2Results;
	private static HashMap<String, Long> perft3Results;
	private static HashMap<String, Long> perft4Results;
	private static HashMap<String, Long> perft5Results;
	private static HashMap<String, Long> perft6Results;
	private static HashMap<String, Long> perft7Results;
	private static HashMap<String, Long> perft8Results;

	@BeforeAll
	public static void setup() throws IOException, ClassNotFoundException {
		position = new Position();
		position.setPosition(Position.START_POSITION);
		perft1Results = loadPerftResults(1);
		perft2Results = loadPerftResults(2);
		perft3Results = loadPerftResults(3);
		perft4Results = loadPerftResults(4);
		perft5Results = loadPerftResults(5);
		perft6Results = loadPerftResults(6);
		perft7Results = loadPerftResults(7);
		perft8Results = loadPerftResults(8);
	}

	@Test
	public void testPerft() {
		long start = System.nanoTime();
		assertEquals(perft6Results.get("Nodes searched"), doPerft(6, false));
		long end = System.nanoTime();
		System.out.println((end - start) * 1e-9);
	}

	private static final long doPerft(int depth, boolean debug) {
		if (debug) {
			return doDebugPerft(depth, depth);
		}
		return doPerft(depth);
	}

	private static final long doPerft(int depth) {
		if (depth == 0) {
			return 1;
		}
		MoveList moves = MoveGenerator.generateAllMoves(position);

		long nodes = 0;
		UndoInfo ui = new UndoInfo();
		for (int i = 0; i < moves.moveCount; i++) {
			int move = moves.mvs[i];
			if (!position.makeMove(move, ui)) {
				continue;
			}
			nodes += doPerft(depth - 1);
			position.unMakeMove(move, ui);
		}
		return nodes;
	}

	private static final long doDebugPerft(int depth, int currentDepth) {
		if (currentDepth == 0) {
			return 1;
		}
		MoveList moves = MoveGenerator.generateAllMoves(position);

		long nodes = 0;
		long currNodes = 0;

		UndoInfo ui = new UndoInfo();
		for (int i = 0; i < moves.moveCount; i++) {
			int move = moves.mvs[i];

			if (!position.makeMove(move, ui)) {
				continue;
			}
			currNodes = doDebugPerft(depth, currentDepth - 1);
			nodes += currNodes;
			String moveString = Move.decodeMove(move);
			String errorMessage = "";
			if (depth == currentDepth) {
				switch (depth) {
				case 1:
//					errorMessage += " Expected " + perft1Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft1Results.get(moveString) == currNodes);
					break;
				case 2:
//					errorMessage += " Expected " + perft2Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft2Results.get(moveString) == currNodes);
					break;
				case 3:
//					errorMessage += " Expected " + perft3Results.get(moveString) + " nodes but was " + currNodes;
					assertTrue(errorMessage, perft3Results.get(moveString) == currNodes);
					break;
				case 4:
//					errorMessage += " Expected " + perft4Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft4Results.get(moveString) == currNodes);
					break;
				case 5:
//					errorMessage += " Expected " + perft5Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft5Results.get(moveString) == currNodes);
					break;
				case 6:
//					errorMessage += " Expected " + perft6Results.get(moveString) + " nodes but was " + currNodes + " ";
					assertTrue(errorMessage, perft6Results.get(moveString) == currNodes);
					break;
				case 7:
					assertTrue(errorMessage, perft7Results.get(moveString) == currNodes);
				case 8:
					assertTrue(errorMessage, perft8Results.get(moveString) == currNodes);
				}
			}
			position.unMakeMove(move, ui);
		}
		return nodes;
	}

	private static final HashMap<String, Long> loadPerftResults(int perftNum) throws IOException {
		HashMap<String, Long> perftResults = new HashMap<>();
		String fileName = PERFT_RESULTS_PATH + "perft-" + perftNum + "-results.txt";
		List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
		for (String line : lines) {
			if (!line.isBlank()) {
				String[] mapVals = line.split(": ");
				perftResults.put(mapVals[0], Long.valueOf(mapVals[1]));
			}
		}
		return perftResults;
	}

}
