package main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import chess.Piece;
import chess.Position;
import gui.ChessFrame;

public class Main {

	public static void main(String[] args) {

		Position position = new Position();
		position.setPosition("4k3/8/8/8/8/8/p7/4K3 b - - 0 1");
		printBitBoard(position.getOccupancies()[Piece.WHITE]);
		printBitBoard(position.getOccupancies()[Piece.BLACK]);
		printBitBoard(position.getOccupancies()[Piece.BOTH]);

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> new ChessFrame()); // display game screen
	}

	public static void printBitBoard(long bitboard) {
		System.out.println("Bitboard representation:");
		for (int rank = 7; rank >= 0; rank--) {
			for (int file = 0; file < 8; file++) {
				int square = rank * 8 + file;
				System.out.print((bitboard & (1L << square)) != 0 ? "1 " : "0 ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
