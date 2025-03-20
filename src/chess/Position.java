package chess;

import gui.ChessBoardPainter;
import util.BitUtil;
import util.BoardUtil;

public class Position {

	public static final String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
	public static final String TRICKY_POSITION = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";

	public static final int WHITE_KING_CASTLE = 1;
	public static final int WHITE_QUEEN_CASTLE = 2;
	public static final int BLACK_KING_CASTLE = 4;
	public static final int BLACK_QUEEN_CASTLE = 8;

	public static final int ALL_MOVES = 0;
	public static final int CAPTURES = 1;

	private int turn;
	private int epSquare;
	private int castleRights;

	private static final int[] CASTLE_RIGHTS_UPDATES = { 7, 15, 15, 15, 3, 15, 15, 11, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
			15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 13, 15, 15, 15, 12, 15, 15, 14, };

	private int halfMoveClock;
	private int fullMoveCount;

	// Used to draw the pieces on the chess board
	private Piece[] pieces;

	private long[] bitboards;
	private long[] occupancies;

	public Position() {
		pieces = new Piece[64];
		occupancies = new long[3];
		bitboards = new long[PieceType.values().length];

		turn = Piece.WHITE;
		epSquare = -1;
		castleRights = 0;
		halfMoveClock = 0;
		fullMoveCount = 1;
	}

	public boolean makeMove(int move, UndoInfo undoInfo) {
		// TODO Replace reassignments with instance methods
		undoInfo.updateInfo(this);

		int src = Move.getSrc(move);
		int dst = Move.getDst(move);
		int piece = Move.getPiece(move);
		int promotedPiece = Move.getPromotedPiece(move);
		int captureFlag = Move.getCaptureFlag(move);
		int doublePawnPushFlag = Move.getDoublePawnPushFlag(move);
		int enPassantFlag = Move.getEnPassantFlag(move);
		int castleFlag = Move.getCastleFlag(move);

		bitboards[piece] = BitUtil.popBit(bitboards[piece], src);
		bitboards[piece] = BitUtil.setBit(bitboards[piece], dst);

		if (captureFlag != 0) {
			int start = turn == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
			int end = turn == Piece.WHITE ? PieceType.BKING.getKey() : PieceType.WKING.getKey();
			for (int key = start; key <= end; key++) {
				if (BitUtil.getBit(bitboards[key], dst) == 1) {
					bitboards[key] = BitUtil.popBit(bitboards[key], dst);
					undoInfo.capturedPiece = key;
					break;
				}
			}
		}
		if (promotedPiece != 0) {
			int key = turn == Piece.WHITE ? PieceType.WPAWN.getKey() : PieceType.BPAWN.getKey();
			bitboards[key] = BitUtil.popBit(bitboards[key], dst);
			bitboards[promotedPiece] = BitUtil.setBit(bitboards[promotedPiece], dst);
		}
		if (enPassantFlag != 0) {
			int key = turn == Piece.WHITE ? PieceType.BPAWN.getKey() : PieceType.WPAWN.getKey();
			bitboards[key] = BitUtil.popBit(bitboards[key], turn == Piece.WHITE ? dst + 8 : dst - 8);
		}

		epSquare = -1;

		if (doublePawnPushFlag != 0) {
			epSquare = turn == Piece.WHITE ? dst + 8 : dst - 8;
		}
		if (castleFlag != 0) {
			int key = turn == Piece.WHITE ? PieceType.WROOK.getKey() : PieceType.BROOK.getKey();
			int rookSrcSq = turn == Piece.WHITE ?
					dst == BoardUtil.getSquareAsIndex("g1") ?
							BoardUtil.getSquareAsIndex("h1") :
							BoardUtil.getSquareAsIndex("a1") :
					dst == BoardUtil.getSquareAsIndex("g8") ?
							BoardUtil.getSquareAsIndex("h8") :
							BoardUtil.getSquareAsIndex("a8");
			int rookDstSq = turn == Piece.WHITE ?
					rookSrcSq == BoardUtil.getSquareAsIndex("h1") ?
							BoardUtil.getSquareAsIndex("f1") :
							BoardUtil.getSquareAsIndex("d1") :
					rookSrcSq == BoardUtil.getSquareAsIndex("h8") ?
							BoardUtil.getSquareAsIndex("f8") :
							BoardUtil.getSquareAsIndex("d8");

			bitboards[key] = BitUtil.popBit(bitboards[key], rookSrcSq);
			bitboards[key] = BitUtil.setBit(bitboards[key], rookDstSq);
		}
		castleRights &= CASTLE_RIGHTS_UPDATES[src];
		castleRights &= CASTLE_RIGHTS_UPDATES[dst];

		updateOccupancies();
		turn = (turn == Piece.WHITE ? Piece.BLACK : Piece.WHITE);
		int kingSq = turn == Piece.WHITE ?
				BitUtil.getLS1BIndex(bitboards[PieceType.BKING.getKey()]) :
				BitUtil.getLS1BIndex(bitboards[PieceType.WKING.getKey()]);
		if (Bitboard.isSquareAttacked(kingSq, turn, bitboards, occupancies)) {
			// restores previous position if the king was in check or check was unresolved
			unMakeMove(move, undoInfo);
			return false;
		}

//			movePiece(src, dst);
		return true;

	}

	public boolean makeMove(int move, UndoInfo undoInfo, int moveFlag) {
		switch (moveFlag) {
		case CAPTURES:
			if (Move.getCaptureFlag(move) != 0) {
				return makeMove(move, undoInfo);
			}
			return false;
		case ALL_MOVES:
			return makeMove(move, undoInfo);
		default:
			return false;
		}
	}

	public void unMakeMove(int move, UndoInfo undoInfo) {

		this.turn = undoInfo.turn;
		this.epSquare = undoInfo.epSquare;
		this.castleRights = undoInfo.castleRights;
//		this.bitboards = undoInfo.bitboards;
//		this.occupancies = undoInfo.occupancies;
//		this.pieces = undoInfo.pieces;
		int src = Move.getSrc(move);
		int dst = Move.getDst(move);
		long srcMask = 1L << src;
		long dstMask = 1L << dst;
		int pieceType = Move.getPiece(move);
		int pieceCaptured = undoInfo.capturedPiece;
		// Put the captured piece at the destination
		bitboards[pieceCaptured] &= ~srcMask;
		bitboards[pieceCaptured] |= dstMask;

		// Put the moved piece at the source
		bitboards[pieceType] &= ~dstMask;
		bitboards[pieceType] |= srcMask;

		if (Move.getCastleFlag(move) != 0) {
			// Undo castle move
			long rookSrcMask = 0;
			long rookDstMask = 0;
			if (src + 2 == dst) { // King side O-O
				rookSrcMask = 1L << (src + 1);
				rookDstMask = 1L << (src + 3);
			} else if (src - 2 == dst) { // Queen side O-O-O
				rookSrcMask = 1L << (src - 1);
				rookDstMask = 1L << (src - 4);
			}
			int rook = turn == Piece.WHITE ? PieceType.WROOK.getKey() : PieceType.BROOK.getKey();
			bitboards[rook] &= ~rookSrcMask;
			bitboards[rook] |= rookDstMask;
		}

		if (Move.getEnPassantFlag(move) != 0) {
			// Undo en-passant move
			if (turn == Piece.WHITE) {
				bitboards[PieceType.BPAWN.getKey()] |= (1L << (dst + 8));
			} else {
				bitboards[PieceType.WPAWN.getKey()] |= (1L << (dst - 8));
			}
		}

		if (Move.getPromotedPiece(move) != 0) {
			// Undo pawn promotion
			bitboards[Move.getPromotedPiece(move)] &= ~(1L << dst);
		}

		updateOccupancies();

//		if (turn == Piece.WHITE) {
//			halfMoveClock--;
//		}
	}

	public void setPosition(String fenString) {
		// TODO Add regex to match FEN
		String[] fenFields = fenString.split(" ");
		String piecePlacement = expandPiecePlacementString(fenFields[0]);
		this.turn = fenFields[1].equals("w") ? Piece.WHITE : Piece.BLACK;

		String castleAbility = fenFields[2];
		this.castleRights = 0;
		this.castleRights |= castleAbility.contains("K") ? WHITE_KING_CASTLE : 0;
		this.castleRights |= castleAbility.contains("Q") ? WHITE_QUEEN_CASTLE : 0;
		this.castleRights |= castleAbility.contains("q") ? BLACK_QUEEN_CASTLE : 0;
		this.castleRights |= castleAbility.contains("k") ? BLACK_KING_CASTLE : 0;

		this.epSquare = fenFields[3].equals("-") ? -1 : BoardUtil.getSquareAsIndex(fenFields[3]);

		this.halfMoveClock = Integer.valueOf(fenFields[4]);
		this.fullMoveCount = Integer.valueOf(fenFields[5]);

		for (int i = 0; i < bitboards.length; i++) {
			bitboards[i] = 0L;
		}
		for (int i = 0; i < piecePlacement.length(); i++) {
			pieces[i] = null;
			String pieceId = String.valueOf(piecePlacement.charAt(i));
			if (pieceId.equals("0")) {
				continue;
			}
			long occupancy = 1L << i;
			int key = PieceType.getKeyById(pieceId);
			bitboards[key] |= occupancy;
			Piece p = new Piece(PieceType.valueOfId(pieceId));
			p.setX((BoardUtil.getFileFromIndex(i) + ChessBoardPainter.START_FILE) * ChessBoardPainter.TILE_SIZE);
			p.setY((BoardUtil.getRankFromIndex(i) + ChessBoardPainter.START_RANK) * ChessBoardPainter.TILE_SIZE);
			pieces[i] = p;
		}
		for (PieceType p : PieceType.values()) {
			if (p == PieceType.NONE) {
				continue;
			}
			occupancies[p.isWhite() ? Piece.WHITE : Piece.BLACK] |= bitboards[p.getKey()];
			occupancies[Piece.BOTH] |= bitboards[p.getKey()];
		}
	}

	public void updateOccupancies() {
		occupancies[Piece.WHITE] = 0l;
		occupancies[Piece.BLACK] = 0l;
		occupancies[Piece.BOTH] = 0l;
		for (PieceType p : PieceType.values()) {
			if (p == PieceType.NONE) {
				continue;
			}
			occupancies[p.isWhite() ? Piece.WHITE : Piece.BLACK] |= bitboards[p.getKey()];
		}
		occupancies[Piece.BOTH] |= occupancies[Piece.WHITE];
		occupancies[Piece.BOTH] |= occupancies[Piece.BLACK];
	}

	public Piece getPiece(int square) {
		return pieces[square];
	}

	public boolean hasPiece(int square) {
		return getPiece(square) != null;
	}

	public void movePiece(int from, int to) {
		Piece toMove = pieces[from];
		pieces[to] = toMove;
		pieces[from] = null;
	}

	public void setPiece(int piece, int sq) {

	}

	public int getTurn() {
		return turn;
	}

	public int getEpSquare() {
		return epSquare;
	}

	public void setEpSquare(int epSquare) {
		this.epSquare = epSquare;
	}

	public int getCastleRights() {
		return castleRights;
	}

	public void setCastleRights(int castleRights) {
		this.castleRights = castleRights;
	}

	public int getHalfMoveClock() {
		return halfMoveClock;
	}

	public int getFullMoveCount() {
		return fullMoveCount;
	}

	public long[] getBitboards() {
		return bitboards;
	}

	public long[] getOccupancies() {
		return occupancies;
	}

	public Piece[] getPieces() {
		return pieces;
	}

	public void drawAttacks(int turn) {
		System.out.println();
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				int sq = rank * 8 + file;
				if (file == 0) {
					System.out.print("  " + (8 - rank) + " ");
				}
				if (Bitboard.isSquareAttacked(sq, turn, bitboards, occupancies)) {
					System.out.print(" " + 1);
				} else {
					System.out.print(" " + 0);
				}

			}
			System.out.println();
		}
		System.out.println("\n     a b c d e f g h\n");
	}

	public void drawPieces() {
		System.out.println();
		for (int i = 0; i < pieces.length; i++) {
			if (i % 8 == 0) {
				System.out.print("  " + (8 - (i / 8)) + " ");
			}
			if (pieces[i] != null) {
				System.out.print(" " + pieces[i].getPieceType().getId());
			} else {
				System.out.print(" .");
			}
			if ((i + 1) % 8 == 0) {
				System.out.println();
			}
		}

		System.out.println("\n     a b c d e f g h\n");

		System.out.println("     ep: " + (epSquare == -1 ? "-" : BoardUtil.getIndexAsSquare(epSquare)));

		String castle = ((castleRights & WHITE_KING_CASTLE) != 0 ? "K" : "")
				+ ((castleRights & WHITE_QUEEN_CASTLE) != 0 ? "Q" : "")
				+ ((castleRights & BLACK_KING_CASTLE) != 0 ? "k" : "")
				+ ((castleRights & BLACK_QUEEN_CASTLE) != 0 ? "q" : "");
		System.out.println("     castle: " + (castle.isEmpty() ? "-" : castle));
	}

	public void drawBoard() {
		System.out.println();
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				if (file == 0) {
					System.out.print("  " + (8 - rank) + " ");
				}
				int square = rank * 8 + file;

				String piece = "";

				for (int i = 0; i < bitboards.length; i++) {
					if (BitUtil.getBit(bitboards[i], square) == 1) {
						piece = PieceType.getIdByKey(i);
					}
				}

				System.out.print(" " + (piece.isEmpty() ? "." : piece));
			}
			System.out.println();
		}
		System.out.println("\n     a b c d e f g h\n");

		System.out.println("     ep: " + (epSquare == -1 ? "-" : BoardUtil.getIndexAsSquare(epSquare)));

		String castle = ((castleRights & WHITE_KING_CASTLE) != 0 ? "K" : "")
				+ ((castleRights & WHITE_QUEEN_CASTLE) != 0 ? "Q" : "")
				+ ((castleRights & BLACK_KING_CASTLE) != 0 ? "k" : "")
				+ ((castleRights & BLACK_QUEEN_CASTLE) != 0 ? "q" : "");
		System.out.println("     castle: " + (castle.isEmpty() ? "-" : castle));
	}

	private String expandPiecePlacementString(String fenPiecePlacement) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < fenPiecePlacement.length(); i++) {
			char symbol = fenPiecePlacement.charAt(i);
			if (symbol == '/') {
				continue;
			}
			if (Character.isDigit(symbol)) {
				sb.append("0".repeat(symbol - '0'));
			} else {
				sb.append(symbol);
			}
		}
		return sb.toString();
	}

	public String getFenString() {
		StringBuffer sb = new StringBuffer();

		for (int rank = 0; rank < 8; rank++) {
			int emptySquares = 0;
			for (int file = 0; file < 8; file++) {
				int sq = rank * 8 + file;
				PieceType piece = PieceType.NONE;
				for (int key = 0; key < bitboards.length; key++) {
					if (BitUtil.getBit(bitboards[key], sq) == 1) {
						piece = PieceType.valueOfKey(key);
						break;
					}
				}
				if (piece != PieceType.NONE) {
					if (emptySquares != 0) {
						sb.append(emptySquares);
					}
					sb.append(piece.getId());
					emptySquares = 0;
				} else {
					emptySquares++;
				}
			}
			if (emptySquares > 0) {
				sb.append(emptySquares);
			}
			if (rank != 7) {
				sb.append("/");
			}
		}
		sb.append(" ");
		sb.append(turn == Piece.WHITE ? "w " : "b ");
		String castle = ((castleRights & WHITE_KING_CASTLE) != 0 ? "K" : "")
				+ ((castleRights & WHITE_QUEEN_CASTLE) != 0 ? "Q" : "")
				+ ((castleRights & BLACK_KING_CASTLE) != 0 ? "k" : "")
				+ ((castleRights & BLACK_QUEEN_CASTLE) != 0 ? "q" : "");
		sb.append(castle.isEmpty() ? "- " : castle + " ");
		sb.append(epSquare == -1 ? "- " : BoardUtil.getIndexAsSquare(epSquare) + " ");
		sb.append(halfMoveClock);
		sb.append(" ");
		sb.append(fullMoveCount);

		return sb.toString();
	}

}
