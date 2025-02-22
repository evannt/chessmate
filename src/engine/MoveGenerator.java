package engine;

import chess.Bitboard;
import chess.Move;
import chess.Piece;
import chess.PieceType;
import chess.Position;
import util.BitUtil;
import util.BoardUtil;

public class MoveGenerator {

	// TODO Rename to Generator

	// TODO Organize code by adding private methods that handle individual move generation

	public MoveGenerator() {

	}

	public static final class MoveList {
		public final Move[] moves;
		public int moveCount;
		private static final int MAX_MOVES = 256;

		public MoveList() {
			moves = new Move[MAX_MOVES];
			moveCount = 0;
		}

		public void addMove(Move move) {
			moves[moveCount] = move;
			moveCount++;
		}

		public void printMoves() {
			for (int i = 0; i < moveCount; i++) {
				System.out.println(moves[i].decodeMove());
			}
		}

//		public void sortMoves(Position pos) {
//			scoreMoves(pos);
//			Arrays.sort(moves, Comparator.nullsLast(Comparator.comparingInt(Move::getScore).reversed()));
//		}
//
//		private void scoreMoves(Position pos) {
//			for (int i = 0; i < moveCount; i++) {
//				moves[i].scoreMove(pos);
//			}
//		}

	}

	public static final MoveList generateAllMoves(Position position) {
		MoveList moveList = new MoveList();
		int src, dst;
		long bitboard, attacks;
		long[] bitboards = position.getBitboards();
		long[] occupancies = position.getOccupancies();
		long bothOccupancies = occupancies[Piece.BOTH];

		for (int piece = PieceType.WPAWN.getKey(); piece < PieceType.BKING.getKey(); piece++) {
			bitboard = bitboards[piece];

			// generate white pawns & white king castling moves
			if (position.getTurn() == Piece.WHITE) {
				if (piece == PieceType.WPAWN.getKey()) {
					while (bitboard != 0) {
						src = BitUtil.getLS1BIndex(bitboard);
						dst = src - 8;
						long srcBitboard = BitUtil.setBit(0L, src);

						// quiet pawn moves
						if (dst >= 0 && BitUtil.getBit(bothOccupancies, dst) == 0) {
							// pawn promotion
							if ((srcBitboard & Bitboard.RANK7) != 0) {
								moveList.addMove(new Move(src, dst, piece, PieceType.WQUEEN.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WROOK.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WBISHOP.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WKNIGHT.getKey(), 0, 0, 0, 0));
							} else {
								// single pawn push
								moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
								// double pawn push
								if ((srcBitboard & Bitboard.RANK2) != 0 && BitUtil.getBit(bothOccupancies, dst - 8) == 0) {
									moveList.addMove(new Move(src, dst - 8, piece, 0, 0, 1, 0, 0));
								}
							}
						}

						attacks = Bitboard.getPawnAttacks(position.getTurn(), src) & position.getOccupancies()[Piece.BLACK];
						while (attacks != 0) {
							dst = BitUtil.getLS1BIndex(attacks);
							// pawn capture promotion
							if ((srcBitboard & Bitboard.RANK7) != 0) {
								// add move into the move list
								moveList.addMove(new Move(src, dst, piece, PieceType.WQUEEN.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WROOK.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WBISHOP.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.WKNIGHT.getKey(), 1, 0, 0, 0));
							} else {
								// pawn capture
								moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
							}
							attacks = BitUtil.popBit(attacks, dst);
						}

						// generate ep attacks
						int epSquare;
						if ((epSquare = position.getEpSquare()) != -1) {
							long epAttacks = Bitboard.getPawnAttacks(position.getTurn(), src) & (1L << epSquare);
							if (epAttacks != 0) {
								int epTarget = BitUtil.getLS1BIndex(epAttacks);
								moveList.addMove(new Move(src, epTarget, piece, 0, 1, 0, 1, 0));
							}
						}

						bitboard = BitUtil.popBit(bitboard, src);
					}
				}
				// castling moves
				if (piece == PieceType.WKING.getKey()) {
					// king side castle
					if ((position.getCastleRights() & Position.WHITE_KING_CASTLE) != 0) {
						// check that squares between king and rook are empty
						int e1 = BoardUtil.getSquareAsIndex("e1");
						int f1 = BoardUtil.getSquareAsIndex("f1");
						int g1 = BoardUtil.getSquareAsIndex("g1");
						if (BitUtil.getBit(bothOccupancies, f1) == 0 && BitUtil.getBit(bothOccupancies, g1) == 0) {
							// make sure the king and the f1 and e1 squares is not under attack
							if (!Bitboard.isSquareAttacked(e1, Piece.BLACK, bitboards, occupancies) &&
									!Bitboard.isSquareAttacked(f1, Piece.BLACK, bitboards, occupancies)) {
								moveList.addMove(new Move(e1, g1, piece, 0, 0, 0, 0, 1));
							}
						}
					}
					// queen side castle
					if ((position.getCastleRights() & Position.WHITE_QUEEN_CASTLE) != 0) {
						int b1 = BoardUtil.getSquareAsIndex("b1");
						int c1 = BoardUtil.getSquareAsIndex("c1");
						int d1 = BoardUtil.getSquareAsIndex("d1");
						int e1 = BoardUtil.getSquareAsIndex("e1");
						if (BitUtil.getBit(bothOccupancies, d1) == 0 && BitUtil.getBit(bothOccupancies, c1) == 0 &&
								BitUtil.getBit(bothOccupancies, b1) == 0) {
							// make sure the king and the f1 and e1 squares is not under attack
							if (!Bitboard.isSquareAttacked(e1, Piece.BLACK, bitboards, occupancies) &&
									!Bitboard.isSquareAttacked(d1, Piece.BLACK, bitboards, occupancies)) {
								moveList.addMove(new Move(e1, c1, piece, 0, 0, 0, 0, 1));
							}
						}
					}
				}
			}
			// generate black pawns & black king castling moves
			else {
				if (piece == PieceType.BPAWN.getKey()) {
					while (bitboard != 0) {
						src = BitUtil.getLS1BIndex(bitboard);
						dst = src + 8;
						long srcBitboard = BitUtil.setBit(0L, src);

						// quiet pawn moves
						if (dst <= 63 && BitUtil.getBit(bothOccupancies, dst) == 0) {
							// pawn promotion
							if ((srcBitboard & Bitboard.RANK2) != 0) {
								// add move into the move list
								moveList.addMove(new Move(src, dst, piece, PieceType.BQUEEN.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BROOK.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BBISHOP.getKey(), 0, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BKNIGHT.getKey(), 0, 0, 0, 0));
							} else {
								// single pawn push
								moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
								// double pawn push
								if ((srcBitboard & Bitboard.RANK7) != 0 && BitUtil.getBit(bothOccupancies, dst + 8) == 0) {
									moveList.addMove(new Move(src, dst + 8, piece, 0, 0, 0, 0, 0));
								}
							}
						}

						attacks = Bitboard.getPawnAttacks(position.getTurn(), src) & position.getOccupancies()[Piece.WHITE];
						while (attacks != 0) {
							dst = BitUtil.getLS1BIndex(attacks);
							// pawn capture promotion
							if ((srcBitboard & Bitboard.RANK2) != 0) {
								moveList.addMove(new Move(src, dst, piece, PieceType.BQUEEN.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BROOK.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BBISHOP.getKey(), 1, 0, 0, 0));
								moveList.addMove(new Move(src, dst, piece, PieceType.BKNIGHT.getKey(), 1, 0, 0, 0));

							} else {
								// pawn capture
								moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
							}
							attacks = BitUtil.popBit(attacks, dst);
						}
						// generate ep attacks
						int epSquare;
						if ((epSquare = position.getEpSquare()) != -1) {
							long epAttacks = Bitboard.getPawnAttacks(position.getTurn(), src) & (1L << epSquare);
							if (epAttacks != 0) {
								int epTarget = BitUtil.getLS1BIndex(epAttacks);
								moveList.addMove(new Move(src, epTarget, piece, 0, 1, 0, 1, 0));
							}
						}

						bitboard = BitUtil.popBit(bitboard, src);
					}
				}
				// castling moves
				if (piece == PieceType.BKING.getKey()) {
					// king side castle
					if ((position.getCastleRights() & Position.BLACK_KING_CASTLE) != 0) {
						// check that squares between king and rook are empty
						int e8 = BoardUtil.getSquareAsIndex("e8");
						int f8 = BoardUtil.getSquareAsIndex("f8");
						int g8 = BoardUtil.getSquareAsIndex("g8");
						if (BitUtil.getBit(bothOccupancies, f8) == 0 && BitUtil.getBit(bothOccupancies, g8) == 0) {
							// make sure the king and the f1 and e1 squares is not under attack
							if (!Bitboard.isSquareAttacked(e8, Piece.WHITE, bitboards, occupancies) &&
									!Bitboard.isSquareAttacked(f8, Piece.WHITE, bitboards, occupancies)) {
								moveList.addMove(new Move(e8, g8, piece, 0, 0, 0, 0, 1));
							}
						}
					}
					// queen side castle
					if ((position.getCastleRights() & Position.BLACK_QUEEN_CASTLE) != 0) {
						int b8 = BoardUtil.getSquareAsIndex("b8");
						int c8 = BoardUtil.getSquareAsIndex("c8");
						int d8 = BoardUtil.getSquareAsIndex("f8");
						int e8 = BoardUtil.getSquareAsIndex("e8");
						if (BitUtil.getBit(bothOccupancies, d8) == 0 && BitUtil.getBit(bothOccupancies, c8) == 0 &&
								BitUtil.getBit(bothOccupancies, b8) == 0) {
							// make sure the king and the f1 and e1 squares is not under attack
							if (!Bitboard.isSquareAttacked(e8, Piece.WHITE, bitboards, occupancies) &&
									!Bitboard.isSquareAttacked(d8, Piece.WHITE, bitboards, occupancies)) {
								moveList.addMove(new Move(e8, c8, piece, 0, 0, 0, 0, 1));
							}
						}
					}
				}
			}

			// generate knight moves
			if (position.getTurn() == Piece.WHITE ?
					piece == PieceType.WKNIGHT.getKey() :
					piece == PieceType.BKNIGHT.getKey()) {
				while (bitboard != 0) {
					src = BitUtil.getLS1BIndex(bitboard);
					attacks = Bitboard.getKnightAttacks(src) & (position.getTurn() == Piece.WHITE ?
							~occupancies[Piece.WHITE] :
							~occupancies[Piece.BLACK]);

					while (attacks != 0) {
						dst = BitUtil.getLS1BIndex(attacks);

						// capture
						if (BitUtil.getBit(position.getTurn() == Piece.WHITE ?
								occupancies[Piece.BLACK] :
								occupancies[Piece.WHITE], dst) == 1) {
							moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
						}
						// quiet move
						else {
							moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
						}
						attacks = BitUtil.popBit(attacks, dst);
					}
					bitboard = BitUtil.popBit(bitboard, src);
				}
			}

			// generate bishop moves
			if (position.getTurn() == Piece.WHITE ?
					piece == PieceType.WBISHOP.getKey() :
					piece == PieceType.BBISHOP.getKey()) {
				while (bitboard != 0) {
					src = BitUtil.getLS1BIndex(bitboard);
					attacks = Bitboard.getBishopAttacks(src, bothOccupancies) & (position.getTurn() == Piece.WHITE ?
							~occupancies[Piece.WHITE] :
							~occupancies[Piece.BLACK]);

					while (attacks != 0) {
						dst = BitUtil.getLS1BIndex(attacks);
						// capture
						if (BitUtil.getBit(position.getTurn() == Piece.WHITE ?
								occupancies[Piece.BLACK] :
								occupancies[Piece.WHITE], dst) == 1) {
							moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
						}
						// quiet move
						else {
							moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
						}

						attacks = BitUtil.popBit(attacks, dst);
					}

					bitboard = BitUtil.popBit(bitboard, src);
				}
			}

			// generate rook moves
			if (position.getTurn() == Piece.WHITE ? piece == PieceType.WROOK.getKey() : piece == PieceType.BROOK.getKey()) {
				while (bitboard != 0) {
					src = BitUtil.getLS1BIndex(bitboard);
					attacks = Bitboard.getRookAttacks(src, bothOccupancies) & (position.getTurn() == Piece.WHITE ?
							~occupancies[Piece.WHITE] :
							~occupancies[Piece.BLACK]);

					while (attacks != 0) {
						dst = BitUtil.getLS1BIndex(attacks);
						// capture
						if (BitUtil.getBit(position.getTurn() == Piece.WHITE ?
								occupancies[Piece.BLACK] :
								occupancies[Piece.WHITE], dst) == 1) {
							moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
						}
						// quiet move
						else {
							moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
						}
						attacks = BitUtil.popBit(attacks, dst);
					}
					bitboard = BitUtil.popBit(bitboard, src);
				}
			}

			// generate queen moves
			if (position.getTurn() == Piece.WHITE ?
					piece == PieceType.WQUEEN.getKey() :
					piece == PieceType.BQUEEN.getKey()) {
				while (bitboard != 0) {
					src = BitUtil.getLS1BIndex(bitboard);
					attacks = Bitboard.getQueenAttacks(src, bothOccupancies) & (position.getTurn() == Piece.WHITE ?
							~occupancies[Piece.WHITE] :
							~occupancies[Piece.BLACK]);

					while (attacks != 0) {
						dst = BitUtil.getLS1BIndex(attacks);
						// capture
						if (BitUtil.getBit(position.getTurn() == Piece.WHITE ?
								occupancies[Piece.BLACK] :
								occupancies[Piece.WHITE], dst) == 1) {
							moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
						}
						// quiet move
						else {
							moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
						}

						attacks = BitUtil.popBit(attacks, dst);
					}

					bitboard = BitUtil.popBit(bitboard, src);
				}
			}

			// generate king mvoes
			if (position.getTurn() == Piece.WHITE ? piece == PieceType.WKING.getKey() : piece == PieceType.BKING.getKey()) {
				while (bitboard != 0) {
					src = BitUtil.getLS1BIndex(bitboard);
					attacks = Bitboard.getKingAttacks(src) & (position.getTurn() == Piece.WHITE ?
							~occupancies[Piece.WHITE] :
							~occupancies[Piece.BLACK]);

					while (attacks != 0) {
						dst = BitUtil.getLS1BIndex(attacks);
						// capture
						if (BitUtil.getBit(position.getTurn() == Piece.WHITE ?
								occupancies[Piece.BLACK] :
								occupancies[Piece.WHITE], dst) == 1) {
							moveList.addMove(new Move(src, dst, piece, 0, 1, 0, 0, 0));
						}
						// quiet move
						else {
							moveList.addMove(new Move(src, dst, piece, 0, 0, 0, 0, 0));
						}
						attacks = BitUtil.popBit(attacks, dst);
					}
					bitboard = BitUtil.popBit(bitboard, src);
				}
			}
		}
		return moveList;
	}

}
