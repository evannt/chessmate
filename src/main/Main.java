package main;

import java.util.Scanner;

import chess.Move;
import chess.Position;
import chess.UndoInfo;
import engine.Evaluator;
import engine.MoveGenerator;
import engine.MoveGenerator.MoveList;
import engine.Searcher;

public class Main {

	public static void main(String[] args) {
		Position position = new Position();
		position.setPosition(Position.POSITION_2);

		boolean debug = true;

		if (debug) {
			debugPosition(position);
		}
//		position.setPosition(Position.POSITION_2);
//		position.setPosition("8/8/3Pp3/8/8/8/8/8 w - e7 0 1");
//		position.drawPieces();

//		new ChessFrame(); // display game screen
//		String MIRROR_SCORE[] = {
//				"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
//				"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
//				"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
//				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
//				"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
//				"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
//				"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
//				"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
//		};

	}

	private static void debugPosition(Position position) {
		Scanner sc = new Scanner(System.in);

//		position.setPosition("rnbqkbnr/pppppppp/8/8/8/3P4/PPP1PPPP/RNBQKBNR w KQkq - 0 1");
		position.drawBoard();
		System.out.println(Evaluator.evaluate(position));
		Searcher searcher = new Searcher(position);
		searcher.search(6);

		MoveList moves = MoveGenerator.generateAllMoves(position);
		UndoInfo ui = new UndoInfo();
		for (int mi = 0; mi < moves.moveCount; mi++) {
			Move move = moves.moves[mi];
			if (!position.makeMove(move, ui)) {
				continue;
			}
			sc.nextLine();
			position.drawBoard();
			System.out.println(Evaluator.evaluate(position));
			searcher.search(6);

			sc.nextLine();
			position.unMakeMove(ui);
			position.drawBoard();
			System.out.println(Evaluator.evaluate(position));
		}
		sc.close();
	}

}
