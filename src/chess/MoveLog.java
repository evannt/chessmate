package chess;

import java.util.ArrayList;
import java.util.List;

import engine.MoveGenerator.MoveList;
import util.BitUtil;
import util.BoardUtil;

public class MoveLog {

	// TODO Add Checkmate

	// TODO Add Draw offer

	// TODO Add End of Game

	private List<Integer> moves;
	private List<String> algebraicMoves;

	public MoveLog() {
		moves = new ArrayList<>();
		algebraicMoves = new ArrayList<>();
	}

	public void addMove(Position position, MoveList validMoves, int move) {
		moves.add(move);
		algebraicMoves.add(getAlgebraicNotation(position, validMoves, move));
	}

	public void removeLastMove() {
		moves.remove(moves.size() - 1);
		algebraicMoves.remove(algebraicMoves.size() - 1);
	}

	public String getLog() {
		// TODO Refactor to build a table
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body><pre>");
		for (int i = 0; i < algebraicMoves.size(); i++) {
			if ((i + 1) % 2 == 0) {
				sb.append("     ");
				sb.append(algebraicMoves.get(i));
				sb.append("<br>");
			} else {
				sb.append(" <b>").append((i + 1) / 2 + 1).append("</b>");
				sb.append(".    ");
				sb.append(algebraicMoves.get(i));
			}
		}
		sb.append("</pre></body></html>");
		return sb.toString();
	}

	public static String getAlgebraicNotation(Position position, MoveList validMoves, int move) {
		StringBuffer sb = new StringBuffer();
		// TODO Pass a checkmate flag to add a # to this move if it causes checkmate
		if (Move.getCastleFlag(move) != 0) {
			int file = Move.getDst(move);
			if (file == 6) {
				sb.append("O-O");
			} else {
				sb.append("O-O-O");
			}
		} else {
			int piece = Move.getPiece(move);
			PieceType promotionType = PieceType.valueOfKey(Move.getPromotedPiece(move));
			int src = Move.getSrc(move), dst = Move.getDst(move);
			int rank = BoardUtil.getRankFromIndex(src), file = BoardUtil.getFileFromIndex(src);
			boolean disambiguateRank = false, disambiguateFile = false;

			sb.append(switch (PieceType.valueOfKey(piece)) {
			case WPAWN, BPAWN -> "";
			default -> PieceType.valueOfKey(piece).getId();
			});

			for (int i = 0; i < validMoves.moveCount; i++) {
				int m = validMoves.mvs[i];
				if (Move.getSrc(m) != src && Move.getDst(m) == dst && Move.getPiece(m) == piece) {
					int mRank = BoardUtil.getRankFromIndex(Move.getSrc(m));
					int mFile = BoardUtil.getFileFromIndex(Move.getSrc(m));
					if (rank == mRank) {
						disambiguateFile = true;
					}
					if (file == mFile) {
						disambiguateRank = true;
					}
				}
				if (disambiguateRank && disambiguateFile) {
					break;
				}
			}
			sb.append(disambiguateFile ? BoardUtil.getFileAsLetter(file) : "");
			sb.append(disambiguateRank ? BoardUtil.getTrueRank(rank) : "");

			sb.append(switch (Move.getCaptureFlag(move)) {
			case 0 -> "";
			default -> "x";
			});

			sb.append(BoardUtil.getIndexAsSquare(Move.getDst(move)));

			sb.append(switch (promotionType) {
			case NONE -> "";
			default -> "=" + promotionType.getId();
			});
		}
		int kingSq = position.getTurn() == Piece.WHITE
				? BitUtil.getLS1BIndex(position.getBitboards()[PieceType.WKING.getKey()])
				: BitUtil.getLS1BIndex(position.getBitboards()[PieceType.BKING.getKey()]);
		int opponent = position.getTurn() == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
		if (Bitboard.isSquareAttacked(kingSq, opponent, position.getBitboards(), position.getOccupancies())) {
			sb.append("+");
		}

		return sb.toString();
	}

}
