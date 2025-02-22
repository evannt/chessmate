package util;

public class BoardUtil {

	public static int getSquareAsIndex(String square) {
		return ((8 - (square.charAt(1) - '0')) * 8 + getFileFromLetter(square.charAt(0)));
	}

	public static String getIndexAsSquare(int index) {
//		int rank = index / 8;
//		int file = index % 8;
		return getFileAsLetter(getFileFromIndex(index)) + getTrueRank(getRankFromIndex(index));
	}

	public static int getIndexFromCoordinate(int rank, int file) {
		return rank * 8 + file;
	}

	public static int getRankFromIndex(int index) {
		return index / 8;
	}

	public static int getFileFromIndex(int index) {
		return index % 8;
	}

	public static int getTrueRank(int rank) {
		return Math.abs(rank - 7) + 1;
	}

	public static String getFileAsLetter(int file) {
		return switch (file) {
		case 0 -> "a";
		case 1 -> "b";
		case 2 -> "c";
		case 3 -> "d";
		case 4 -> "e";
		case 5 -> "f";
		case 6 -> "g";
		case 7 -> "h";
		default -> "";
		};
	}

	public static int getFileFromLetter(char file) {
		return switch (file) {
		case 'a' -> 0;
		case 'b' -> 1;
		case 'c' -> 2;
		case 'd' -> 3;
		case 'e' -> 4;
		case 'f' -> 5;
		case 'g' -> 6;
		case 'h' -> 7;
		default -> -1;
		};
	}
}
