package main;

import java.util.Scanner;

import javax.swing.SwingUtilities;

import chess.Position;
import chess.UndoInfo;
import engine.Evaluator;
import engine.MoveGenerator;
import engine.MoveGenerator.MoveList;
import engine.Searcher;
import gui.ChessFrame;

public class Main {

	public static void main(String[] args) {
		Position position = new Position();
		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");

		boolean debug = false;

		if (debug) {
			debugPosition(position);
		}

		SwingUtilities.invokeLater(() -> new ChessFrame()); // display game screen
	}

	private static void debugPosition(Position position) {
		Scanner sc = new Scanner(System.in);

		position.setPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");
		position.drawBoard();
		System.out.println(Evaluator.evaluate(position));
		Searcher searcher = new Searcher(position);
		searcher.search(1);

		MoveList moves = MoveGenerator.generateAllMoves(position);
		UndoInfo ui = new UndoInfo();
		for (int mi = 0; mi < moves.moveCount; mi++) {
			int move = moves.mvs[mi];
			if (!position.makeMove(move, ui)) {
				continue;
			}
			sc.nextLine();
			position.drawBoard();
			System.out.println(Evaluator.evaluate(position));
			searcher.search(1);
			System.out.println(position.getFenString());

			sc.nextLine();
			position.unMakeMove(move, ui);
			position.drawBoard();
			System.out.println(Evaluator.evaluate(position));
		}
		sc.close();
	}

}
