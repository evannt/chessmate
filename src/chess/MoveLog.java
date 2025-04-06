package chess;

import java.util.ArrayList;
import java.util.List;

import engine.MoveGenerator.MoveList;
import util.BoardUtil;

public class MoveLog {

	// TODO Add Checkmate

	// TODO Add Draw offer

	// TODO Add End of Game

	private static final String GRAY_HEX = "#656565";
	private static final String LIGHT_GRAY_HEX = "#555555";

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

	public int getLastMove() {
		int moveIndex = moves.size() - 1;
		return moveIndex < 0 ? -1 : moves.get(moveIndex);
	}

	public void removeLastMove() {
		moves.remove(moves.size() - 1);
		algebraicMoves.remove(algebraicMoves.size() - 1);
	}

	public String getLog() {
		StringBuffer sb = new StringBuffer();
		int moveCount = algebraicMoves.size();
		String rowColor = GRAY_HEX;
		sb.append("<html>");
		sb.append("<body>");
		sb.append("<table cellpadding='2' cellspacing='0' style='width: 100%; border-collapse: collapse;'>");
		for (int i = 0; i < moveCount; i++) {
			if ((i + 1) % 2 == 0) {
				sb.append("<td>").append(algebraicMoves.get(i)).append("</td></tr>"); // Black move
				rowColor = rowColor == GRAY_HEX ? LIGHT_GRAY_HEX : GRAY_HEX;
			} else {
				sb.append("<tr").append(" style='background-color:").append(rowColor).append(";'>");
				sb.append("<td style='padding-left: 10;'><b>").append((i + 1) / 2 + 1).append(".</b></td>"); // Number
				sb.append("<td>").append(algebraicMoves.get(i)).append("</td>"); // White move
			}
		}
		sb.append(moveCount % 2 == 1 ? "<td>&nbsp;&nbsp;</td></tr>" : "");
		sb.append("</table></body></html>");
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
		sb.append(position.isInCheck() ? "+" : "");

		return sb.toString();
	}

}
