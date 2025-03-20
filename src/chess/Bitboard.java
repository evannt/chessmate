package chess;

import util.BitUtil;
import util.RandomUtil;

public class Bitboard {

	// TODO Optimize initilization

	public static final long NOT_A_FILE = 0xFEFEFEFEFEFEFEFEL;
	public static final long NOT_H_FILE = 0x7F7F7F7F7F7F7F7FL;
	public static final long NOT_HG_FILE = 0x3F3F3F3F3F3F3F3FL;
	public static final long NOT_AB_FILE = 0xFCFCFCFCFCFCFCFCL;

	public static final long RANK1 = 0xFF00000000000000L;
	public static final long RANK2 = 0x00FF000000000000L;
	public static final long RANK3 = 0x0000FF0000000000L;
	public static final long RANK4 = 0x000000FF00000000L;
	public static final long RANK5 = 0x00000000FF000000L;
	public static final long RANK6 = 0x0000000000FF0000L;
	public static final long RANK7 = 0x000000000000FF00L;
	public static final long RANK8 = 0x00000000000000FFL;

	public static final long A_FILE = 0x0101010101010101L;
	public static final long B_FILE = 0x0202020202020202L;
	public static final long C_FILE = 0x404040404040404L;
	public static final long D_FILE = 0x0808080808080808L;
	public static final long E_FILE = 0x1010101010101010L;
	public static final long F_FILE = 0x2020202020202020L;
	public static final long G_FILE = 0x4040404040404040L;
	public static final long H_FILE = 0x8080808080808080L;

	private static final int[] RELEVANT_BISHOP_BITS = { 6, 5, 5, 5, 5, 5, 5, 6, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 7, 7,
			7, 5, 5, 5, 5, 7, 9, 9, 7, 5, 5, 5, 5, 7, 9, 9, 7, 5, 5, 5, 5, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6,
			5, 5, 5, 5, 5, 5, 6 };

	private static final long BISHOP_MAGICS[] = { 0x5048200440504100L, 0x102288004a8002L, 0x410c088082820aL,
			0x20082080210040a0L, 0x1104000401400L, 0x44010420c4418002L, 0x260420820092000L, 0x12420041084010L,
			0x2000041010511110L, 0xc4040c104a03L, 0x4006881084008110L, 0x82481000100L, 0x8402840420058010L,
			0x621212808c00000L, 0x20008241202000L, 0x8008300423004c5L, 0x4928081020d90400L, 0x10c40c6004009a10L,
			0x1180202040100L, 0x2121005820460004L, 0x1004100202022400L, 0x204100a02008402L, 0x800800108080240L,
			0x1000024010468L, 0x3090a009200100L, 0x41c0830039804L, 0x4224100041010020L, 0x40808008020002L,
			0x8001001045004000L, 0x110004154805000L, 0x2008c20424050408L, 0x602210a010105L, 0x18020800916021L,
			0x2082222000100102L, 0x102108800300044L, 0x30902008000b0050L, 0x804080200002008L, 0x880b02080010080L,
			0x1002220040041400L, 0x8012920c30980L, 0x40410188c0012021L, 0x4032838000222L, 0x21000c004804a401L,
			0xc10182018000100L, 0xe01210020a009020L, 0x244108802000040L, 0x182020802310100L, 0x895040020008cL,
			0x200118020a618001L, 0x116250150100800L, 0x1000044141c0000L, 0xc000014022880e08L, 0x200000405040402L,
			0x89880a2218420000L, 0x40048400820120L, 0x84218409120010L, 0x402100480088880aL, 0x8030008201012010L,
			0x600a82440c0400L, 0x1002081400208802L, 0x1404804a0024c10L, 0x4010582004014202L, 0xa20204822180049L,
			0x2820404268200L, };

	private static final int[] RELEVANT_ROOK_BITS = { 12, 11, 11, 11, 11, 11, 11, 12, 11, 10, 10, 10, 10, 10, 10, 11,
			11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 11, 10, 10,
			10, 10, 10, 10, 11, 11, 10, 10, 10, 10, 10, 10, 11, 12, 11, 11, 11, 11, 11, 11, 12 };

	private static final long ROOK_MAGICS[] = { 0x80102040008000L, 0x8400a20001001c0L, 0x100200010090042L,
			0x2080040800801000L, 0x200204850840200L, 0x200100104080200L, 0x200020001408804L, 0x8200010080402a04L,
			0x11c800040002081L, 0x41804000806008L, 0x863001020010044L, 0x102000a20104201L, 0x1001008010004L,
			0x400800200040080L, 0xa00808002000100L, 0x881000894422100L, 0x8288004400081L, 0x4848020004000L,
			0x4101090020004010L, 0x404220010400a00L, 0xa3010008000410L, 0x180808004000200L, 0x4400098a1810L,
			0x4200020000890844L, 0x10a0208080004003L, 0x2880200040005000L, 0x8420002100410010L, 0x2200080080100080L,
			0x200040080800800L, 0x40080800200L, 0x4004010080800200L, 0x2000004200008104L, 0x40004262800080L,
			0x30004002402001L, 0x800802000801000L, 0x20c1002009001004L, 0x2040802402800800L, 0xa0004d2001008L,
			0x2040488104001002L, 0x3004082000104L, 0x802040008000L, 0x820100841254000L, 0x3820041001868020L,
			0x9001011004210008L, 0x20080004008080L, 0x5100040002008080L, 0x2090508102040028L, 0x1400010040820004L,
			0x121800040122a80L, 0xc204009008300L, 0x401001444200100L, 0x20815000080180L, 0x222000410082200L,
			0x980040002008080L, 0x4106220110486400L, 0x211000042008100L, 0x6000144081002202L, 0x8040001b006381L,
			0x88402000100901L, 0x200081000210055L, 0x102002008100402L, 0x201a000408011082L, 0x1000589008010204L,
			0x80a518621004c02L, };

	private static final long[] bishopMasks;
	private static final long[] rookMasks;

	private static final long[][] pawnAttacks;
	private static final long[] knightAttacks;
	public static final long[][] bishopAttacks;
	private static final long[][] rookAttacks;
	private static final long[] kingAttacks;

	static {
		bishopMasks = new long[64];
		rookMasks = new long[64];

		pawnAttacks = new long[2][64];
		knightAttacks = new long[64];
		bishopAttacks = new long[64][512];
		rookAttacks = new long[64][4096];
		kingAttacks = new long[64];

		generateAttacks();
	}

//	public Bitboard() {
//		bishopMasks = new long[64];
//		rookMasks = new long[64];
//
//		pawnAttacks = new long[2][64];
//		knightAttacks = new long[64];
//		bishopAttacks = new long[64][512];
//		rookAttacks = new long[64][4096];
//		kingAttacks = new long[64];
//
//		generateAttacks();
//	}

	private static final void generateAttacks() {
		for (int sq = 0; sq < 64; sq++) {
			pawnAttacks[Piece.WHITE][sq] = maskPawnAttacks(Piece.WHITE, sq);
			pawnAttacks[Piece.BLACK][sq] = maskPawnAttacks(Piece.BLACK, sq);
			knightAttacks[sq] = maskKnightAttacks(sq);
			kingAttacks[sq] = maskKingAttacks(sq);

			bishopMasks[sq] = maskBishopAttacks(sq);
			rookMasks[sq] = maskRookAttacks(sq);

			long bishopAttackMask = bishopMasks[sq];

			int relevantBits = Long.bitCount(bishopAttackMask);
			int occupancyIndicies = 1 << relevantBits;

			for (int i = 0; i < occupancyIndicies; i++) {
				long occupancy = setOccupancy(i, relevantBits, bishopAttackMask);
				int magicIndex = (int) ((occupancy * BISHOP_MAGICS[sq]) >>> (64 - relevantBits));
				bishopAttacks[sq][magicIndex] = maskBishopAttacks(sq, occupancy);
			}

			long rookAttackMask = rookMasks[sq];
			relevantBits = Long.bitCount(rookAttackMask);
			occupancyIndicies = 1 << relevantBits;

			for (int i = 0; i < occupancyIndicies; i++) {
				long occupancy = setOccupancy(i, relevantBits, rookAttackMask);
				int magicIndex = (int) ((occupancy * ROOK_MAGICS[sq]) >>> (64 - relevantBits));
				rookAttacks[sq][magicIndex] = maskRookAttacks(sq, occupancy);
			}
		}
	}

	public static boolean isSquareAttacked(int square, int turn, long[] bitboards, long[] occupancies) {
		// TODO Optimize further
		if (turn == Piece.WHITE) {
			if ((getKnightAttacks(square) & bitboards[PieceType.WKNIGHT.getKey()]) != 0) {
				return true;
			}
			if ((getKingAttacks(square) & bitboards[PieceType.WKING.getKey()]) != 0) {
				return true;
			}
			if ((getPawnAttacks(Piece.BLACK, square) & bitboards[PieceType.WPAWN.getKey()]) != 0) {
				return true;
			}
			if ((getBishopAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.WBISHOP.getKey()]) != 0) {
				return true;
			}
			if ((getRookAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.WROOK.getKey()]) != 0) {
				return true;
			}
			if ((getQueenAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.WQUEEN.getKey()]) != 0) {
				return true;
			}
		} else {
			if ((getKnightAttacks(square) & bitboards[PieceType.BKNIGHT.getKey()]) != 0) {
				return true;
			}
			if ((getKingAttacks(square) & bitboards[PieceType.BKING.getKey()]) != 0) {
				return true;
			}
			if ((getPawnAttacks(Piece.WHITE, square) & bitboards[PieceType.BPAWN.getKey()]) != 0) {
				return true;
			}
			if ((getBishopAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.BBISHOP.getKey()]) != 0) {
				return true;
			}
			if ((getRookAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.BROOK.getKey()]) != 0) {
				return true;
			}
			if ((getQueenAttacks(square, occupancies[Piece.BOTH]) & bitboards[PieceType.BQUEEN.getKey()]) != 0) {
				return true;
			}
		}
		return false;
	}

	public static long getPawnAttacks(int turn, int square) {
		return pawnAttacks[turn][square];
	}

	public static long getKnightAttacks(int square) {
		return knightAttacks[square];
	}

	public static long getBishopAttacks(int square, long occupancy) {
		occupancy &= bishopMasks[square];
		occupancy *= BISHOP_MAGICS[square];
		occupancy >>>= (64 - RELEVANT_BISHOP_BITS[square]);
		return bishopAttacks[square][(int) occupancy];
	}

	public static long getRookAttacks(int square, long occupancy) {
		occupancy &= rookMasks[square];
		occupancy *= ROOK_MAGICS[square];
		occupancy >>>= (64 - RELEVANT_ROOK_BITS[square]);
		return rookAttacks[square][(int) occupancy];
	}

	public static long getQueenAttacks(int square, long occupancy) {
		return getBishopAttacks(square, occupancy) | getRookAttacks(square, occupancy);
	}

	public static final long getKingAttacks(int square) {
		return kingAttacks[square];
	}

	private static final long maskPawnAttacks(int turn, int square) {
		long attacks = 0L;
		long bitboard = BitUtil.setBit(0L, square);

		if (turn == Piece.WHITE) {
			if (((bitboard >>> 7) & NOT_A_FILE) != 0) {
				attacks |= (bitboard >>> 7);
			}
			if (((bitboard >>> 9) & NOT_H_FILE) != 0) {
				attacks |= (bitboard >>> 9);
			}
//			return ((bitboard >>> 7) & NOT_A_FILE) | ((bitboard >>> 9) & NOT_H_FILE);
		} else {
			if (((bitboard << 7) & NOT_H_FILE) != 0) {
				attacks |= (bitboard << 7);
			}
			if (((bitboard << 9) & NOT_A_FILE) != 0) {
				attacks |= (bitboard << 9);
			}
		}
		return attacks;
//		return ((bitboard << 7) & NOT_H_FILE) | ((bitboard << 9) & NOT_A_FILE);
	}

	private static final long maskKnightAttacks(int square) {
		long attacks = 0L;
		long bitboard = BitUtil.setBit(0L, square);

		if (((bitboard >>> 17) & NOT_H_FILE) != 0) {
			attacks |= (bitboard >>> 17);
		}
		if (((bitboard >>> 15) & NOT_A_FILE) != 0) {
			attacks |= (bitboard >>> 15);
		}
		if (((bitboard >>> 10) & NOT_HG_FILE) != 0) {
			attacks |= (bitboard >>> 10);
		}
		if (((bitboard >>> 6) & NOT_AB_FILE) != 0) {
			attacks |= (bitboard >>> 6);
		}
		if (((bitboard << 17) & NOT_A_FILE) != 0) {
			attacks |= (bitboard << 17);
		}
		if (((bitboard << 15) & NOT_H_FILE) != 0) {
			attacks |= (bitboard << 15);
		}
		if (((bitboard << 10) & NOT_AB_FILE) != 0) {
			attacks |= (bitboard << 10);
		}
		if (((bitboard << 6) & NOT_HG_FILE) != 0) {
			attacks |= (bitboard << 6);
		}
		return attacks;
//		return (((bitboard << 15) | (bitboard >>> 17)) & NOT_H_FILE) |
//				(((bitboard << 17) | (bitboard >>> 15)) & NOT_A_FILE) |
//				(((bitboard << 6) | (bitboard >>> 10)) & NOT_HG_FILE) |
//				(((bitboard << 10) | (bitboard >>> 6)) & NOT_AB_FILE);
	}

	private static final long maskKingAttacks(int square) {
		long attacks = 0L;
		long bitboard = BitUtil.setBit(0L, square);

		if ((bitboard >>> 8) != 0) {
			attacks |= (bitboard >>> 8);
		}
		if (((bitboard >>> 9) & NOT_H_FILE) != 0) {
			attacks |= (bitboard >>> 9);
		}
		if (((bitboard >>> 7) & NOT_A_FILE) != 0) {
			attacks |= (bitboard >>> 7);
		}
		if (((bitboard >>> 1) & NOT_H_FILE) != 0) {
			attacks |= (bitboard >>> 1);
		}
		if ((bitboard << 8) != 0) {
			attacks |= (bitboard << 8);
		}
		if (((bitboard << 9) & NOT_A_FILE) != 0) {
			attacks |= (bitboard << 9);
		}
		if (((bitboard << 7) & NOT_H_FILE) != 0) {
			attacks |= (bitboard << 7);
		}
		if (((bitboard << 1) & NOT_A_FILE) != 0) {
			attacks |= (bitboard << 1);
		}
		return attacks;
//		return (bitboard >>> 8) | (bitboard << 8) |
//				(((bitboard << 1) | (bitboard >>> 7) | (bitboard << 9)) & NOT_A_FILE) |
//				(((bitboard >>> 1) | (bitboard << 7) | (bitboard >>> 9)) & NOT_H_FILE);
	}

	private static final long maskBishopAttacks(int square) {
		long attacks = 0L;
		int tr = square / 8;
		int tf = square % 8;

		for (int rank = tr + 1, file = tf + 1; rank <= 6 && file <= 6; rank++, file++) {
			attacks |= (1L << (rank * 8 + file));
		}
		for (int rank = tr - 1, file = tf + 1; rank >= 1 && file <= 6; rank--, file++) {
			attacks |= (1L << (rank * 8 + file));
		}
		for (int rank = tr + 1, file = tf - 1; rank <= 6 && file >= 1; rank++, file--) {
			attacks |= (1L << (rank * 8 + file));
		}
		for (int rank = tr - 1, file = tf - 1; rank >= 1 && file >= 1; rank--, file--) {
			attacks |= (1L << (rank * 8 + file));
		}
		return attacks;
	}

	private static final long maskBishopAttacks(int square, long blocks) {
		long attacks = 0L;
		int tr = square / 8;
		int tf = square % 8;

		for (int rank = tr + 1, file = tf + 1; rank <= 7 && file <= 7; rank++, file++) {
			long sq = (1L << (rank * 8 + file));
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int rank = tr - 1, file = tf + 1; rank >= 0 && file <= 7; rank--, file++) {
			long sq = (1L << (rank * 8 + file));
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int rank = tr + 1, file = tf - 1; rank <= 7 && file >= 0; rank++, file--) {
			long sq = (1L << (rank * 8 + file));
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int rank = tr - 1, file = tf - 1; rank >= 0 && file >= 0; rank--, file--) {
			long sq = (1L << (rank * 8 + file));
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		return attacks;
	}

	private static long maskRookAttacks(int square) {
		long attacks = 0L;
		int tr = square / 8;
		int tf = square % 8;

		for (int rank = tr + 1; rank <= 6; rank++) {
			attacks |= (1L << (rank * 8 + tf));
		}
		for (int rank = tr - 1; rank >= 1; rank--) {
			attacks |= (1L << (rank * 8 + tf));
		}
		for (int file = tf + 1; file <= 6; file++) {
			attacks |= (1L << (tr * 8 + file));
		}
		for (int file = tf - 1; file >= 1; file--) {
			attacks |= (1L << (tr * 8 + file));
		}
		return attacks;
	}

	private static final long maskRookAttacks(int square, long blocks) {
		long attacks = 0L;
		int tr = square / 8;
		int tf = square % 8;

		for (int rank = tr + 1; rank <= 7; rank++) {
			long sq = 1L << (rank * 8 + tf);
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int rank = tr - 1; rank >= 0; rank--) {
			long sq = 1L << (rank * 8 + tf);
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int file = tf + 1; file <= 7; file++) {
			long sq = 1L << (tr * 8 + file);
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		for (int file = tf - 1; file >= 0; file--) {
			long sq = 1L << (tr * 8 + file);
			attacks |= sq;
			if ((sq & blocks) != 0) {
				break;
			}
		}
		return attacks;
	}

	private static final long setOccupancy(int index, int numMaskBits, long attackMask) {
		long occupancy = 0L;

		for (int i = 0; i < numMaskBits; i++) {
			int square = BitUtil.getLS1BIndex(attackMask);
			attackMask = BitUtil.popBit(attackMask, square);
			if ((index & (1L << i)) != 0L) {
				occupancy |= (1L << square);
//				occupancies = setBit(occupancies, square); // also works
			}
		}

		return occupancy;
	}

	private static final long getMagicNumber(int square, int relevantBits, boolean bishop) {
		long[] occupancies = new long[4096];
		long[] attacks = new long[4096];
		long[] usedAttacks = new long[4096];
		long attackMask = bishop ? maskBishopAttacks(square) : maskRookAttacks(square);
		int occupancyIndicies = 1 << relevantBits;

		for (int i = 0; i < occupancyIndicies; i++) {
			occupancies[i] = setOccupancy(i, relevantBits, attackMask);
			attacks[i] = bishop ? maskBishopAttacks(square, occupancies[i]) : maskRookAttacks(square, occupancies[i]);
		}

		for (int r = 0; r < 1000000000; r++) {
			long magicNumber = RandomUtil.getMagicNumber();
			if (Long.bitCount((attackMask * magicNumber) & 0xFF00000000000000L) < 6) {
				continue;
			}
			for (int i = 0; i < 4096; i++) {
				usedAttacks[i] = 0;
			}
			boolean failed = false;
			for (int k = 0; !failed && k < occupancyIndicies; k++) {
				int magicIndex = (int) ((occupancies[k] * magicNumber) >>> (64 - relevantBits));
				if (usedAttacks[magicIndex] == 0L) {
					usedAttacks[magicIndex] = attacks[k];
				} else if (usedAttacks[magicIndex] != attacks[k]) {
					failed = true;
				}
			}
			if (!failed) {
				return magicNumber;
			}
		}
		throw new RuntimeException("Failed to generate magic numbers for " + (bishop ? "bishops" : "rooks"));
//		return 0L;
	}

	public static void generateMagicNumbers() {
		System.out.println("private static final long ROOK_MAGICS[] = {");
		for (int sq = 0; sq < 64; sq++) {
			if ((sq + 1) % 4 == 0) {
				System.out.print("0x" + Long.toHexString(getMagicNumber(sq, RELEVANT_ROOK_BITS[sq], false)) + "L,\n");
			} else {
				System.out.print("0x" + Long.toHexString(getMagicNumber(sq, RELEVANT_ROOK_BITS[sq], false)) + "L, ");
			}
		}
		System.out.println("};");
		System.out.println("private static final long BISHOP_MAGICS[] = {");
		for (int sq = 0; sq < 64; sq++) {
			if ((sq + 1) % 4 == 0) {
				System.out.print("0x" + Long.toHexString(getMagicNumber(sq, RELEVANT_BISHOP_BITS[sq], true)) + "L,\n");
			} else {
				System.out.print("0x" + Long.toHexString(getMagicNumber(sq, RELEVANT_BISHOP_BITS[sq], true)) + "L, ");
			}
		}
		System.out.println("};");
	}

	public static final void drawBitboard(long bitboard) {
		System.out.println();
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (file == 0) {
					System.out.print("  " + (8 - rank) + " ");
				}
				int square = rank * 8 + file;
				if (BitUtil.getBit(bitboard, square) == 1l) {
					System.out.print(" " + 1 + " ");
				} else {
					System.out.print(" " + 0 + " ");
				}
			}
			System.out.println();
		}
		System.out.println("\n     a  b  c  d  e  f  g  h\n");
		System.out.println("Bitboard: " + bitboard);
	}

}
