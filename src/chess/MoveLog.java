package chess;

import java.util.ArrayDeque;
import java.util.Deque;

import engine.MoveGenerator.MoveList;
import util.BoardUtil;

public class MoveLog {

	// TODO Add Draw offer

	private static final String GRAY_HEX = "#656565";
	private static final String LIGHT_GRAY_HEX = "#555555";

	private Deque<MoveInfo> moves;
	private Deque<MoveInfo> undos;

	public MoveLog() {
		moves = new ArrayDeque<>();
		undos = new ArrayDeque<>();
	}

	public void addMove(Position position, MoveList validMoves, int move, UndoInfo undoInfo, boolean isCheckmate) {
		if (hasUnMadeMove()) {
			undos.clear();
		}
		moves.push(new MoveInfo(move, getAlgebraicNotation(position, validMoves, move, isCheckmate), undoInfo));
	}

	public boolean hasPreviousMove() {
		return moves.size() != 0;
	}

	public boolean hasUnMadeMove() {
		return undos.size() != 0;
	}

	public int getLastMove() {
		return hasPreviousMove() ? moves.peek().move : -1;
	}

	public MoveInfo getLastMoveInfo() {
		return moves.peek();
	}

	public MoveInfo getUnMadeMoveInfo() {
		return undos.peek();
	}

	public void removeLastMove() {
		if (hasPreviousMove()) {
			undos.push(moves.pop());
		}
	}

	public void addBackLastMove() {
		if (hasUnMadeMove()) {
			moves.push(undos.pop());
		}
	}

	public String getLog() {
		StringBuffer sb = new StringBuffer();
		MoveInfo moveArray[] = moves.toArray(new MoveInfo[0]);
		int moveCount = moveArray.length;
		String rowColor = GRAY_HEX;
		sb.append("<html>");
		sb.append("<body style='font-size: 14px'>");
		sb.append("<table cellpadding='2' cellspacing='0' style='width: 100%; border-collapse: collapse;'>");
		for (int i = moveCount - 1; i >= 0; i--) {
			int moveNum = moveCount - i;
			if ((moveNum + 1) % 2 == 0) {
				sb.append("<tr").append(" style='background-color:").append(rowColor).append(";'>");
				sb.append("<td style='padding-left: 10;'><b>").append(moveNum / 2 + 1).append(".</b></td>"); // Number
				sb.append("<td>").append(moveArray[i].algebraicMove).append("</td>"); // White move
			} else {
				sb.append("<td>").append(moveArray[i].algebraicMove).append("</td></tr>"); // Black move
				rowColor = rowColor == GRAY_HEX ? LIGHT_GRAY_HEX : GRAY_HEX;
			}
		}
		sb.append(moveCount % 2 == 1 ? "<td>&nbsp;&nbsp;</td></tr>" : "");
		sb.append("</table></body></html>");
		return sb.toString();
	}

	public static String getAlgebraicNotation(Position position, MoveList validMoves, int move, boolean isCheckmate) {
		StringBuffer sb = new StringBuffer();
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
		sb.append(isCheckmate ? "#" : position.isInCheck() ? "+" : "");

		return sb.toString();
	}

}
