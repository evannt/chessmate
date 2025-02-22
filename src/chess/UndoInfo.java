package chess;

public class UndoInfo {

	public long[] bitboards;
	public long[] occupancies;
	public Piece[] pieces;
	public int turn;
	public int epSquare;
	public int castleRights;

	@Deprecated
	public UndoInfo(Position position) {
		this.bitboards = position.getBitboards().clone();
		this.occupancies = position.getOccupancies().clone();
		this.turn = position.getTurn();
		this.epSquare = position.getEpSquare();
		this.castleRights = position.getCastleRights();
	}

	public UndoInfo() {
	}

	public void updateInfo(Position position) {
		this.bitboards = position.getBitboards().clone(); // TODO Consider alternative to clone
		this.occupancies = position.getOccupancies().clone();
		this.pieces = position.getPieces().clone(); // TODO Consider alternative to clone
		this.turn = position.getTurn();
		this.epSquare = position.getEpSquare();
		this.castleRights = position.getCastleRights();
	}

}
