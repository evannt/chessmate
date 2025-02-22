package util;

public class BitUtil {

	public static long getBit(long bitboard, int square) {
		return (bitboard >>> square) & 1L;
	}

	public static long setBit(long bitboard, int square) {
		return bitboard |= (1L << square);
	}

	public static long popBit(long bitboard, int square) {
		return bitboard &= ~(1L << square);
	}

	public static int getLS1BIndex(long bitboard) {
		if (bitboard != 0) {
			return Long.bitCount((bitboard & (-1 * bitboard)) - 1);
		}
		return -1;
	}

}
