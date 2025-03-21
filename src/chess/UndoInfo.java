package chess;

public class UndoInfo {

	public Piece[] pieces;
	public int capturedPiece;
	public int turn;
	public int epSquare;
	public int castleRights;

	public void updateInfo(Position position) {
//		this.pieces = Arrays.stream(position.getPieces()).map(piece -> piece == null ? null : new Piece(piece))
//				.toArray(Piece[]::new);
		this.pieces = position.getPieces().clone();
		this.capturedPiece = PieceType.NONE.getKey();
		this.turn = position.getTurn();
		this.epSquare = position.getEpSquare();
		this.castleRights = position.getCastleRights();
	}

}
