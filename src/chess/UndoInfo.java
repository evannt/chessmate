package chess;

public class UndoInfo {

	public int capturedPiece;
	public int turn;
	public int epSquare;
	public int castleRights;

	public void updateInfo(Position position) {
//		this.bitboards = position.getBitboards().clone(); // TODO Consider alternative to clone
//		this.bitboards = Arrays.copyOf(position.getBitboards(), position.getBitboards().length);
//		this.occupancies = position.getOccupancies().clone();
//		this.occupancies = Arrays.copyOf(position.getOccupancies(), position.getOccupancies().length);
//		this.pieces = position.getPieces().clone(); // TODO Consider alternative to clone

		this.turn = position.getTurn();
		this.epSquare = position.getEpSquare();
		this.castleRights = position.getCastleRights();
	}

}
