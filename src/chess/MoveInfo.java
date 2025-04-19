package chess;

public class MoveInfo {

	public final int move;
	public final String algebraicMove;
	public final UndoInfo undoInfo;

	public MoveInfo(int move, String algebraicMove, UndoInfo undoInfo) {
		this.move = move;
		this.algebraicMove = algebraicMove;
		this.undoInfo = undoInfo;
	}

}
